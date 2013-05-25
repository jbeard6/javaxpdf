package com.foolabs.xpdf;

import static java.io.File.createTempFile;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * A service that copies an {@link InputStream} into a temporary file and
 * ensures it is cleaned up appropriately.
 * 
 */
class TemporaryFileService<T> {

	private static final Logger LOGGER = Logger
			.getLogger(TemporaryFileService.class.getName());

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public TemporaryFileService(String prefix, String suffix, int bufferSize) {
		if (prefix == null || suffix == null) {
			throw new NullPointerException();
		} else if (prefix.length() < 3) {
			throw new IllegalArgumentException("prefix.length < 3");
		} else if (bufferSize < 0) {
			throw new IllegalArgumentException(bufferSize + "< 0");
		}

		this.prefix = prefix;
		this.suffix = suffix;
		this.bufferSize = bufferSize;
		this.deletionQueue = new ReferenceQueue<T>();
		this.tempFileMap = new ConcurrentHashMap<Reference<T>, File>();
	}

	public TemporaryFileService(String prefix, String suffix) {
		this(prefix, suffix, DEFAULT_BUFFER_SIZE);
	}

	private final String prefix;

	private final String suffix;

	private final int bufferSize;

	/**
	 * Copy bytes from aan {@link InputStream} to an {@link OutputStream}.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private long copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Creates a temporary {@link File} with the contents of the
	 * {@link InputStream}.
	 * 
	 * @param inputStream
	 *            the contents
	 * @return the temporary file
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public File createTemporaryFile(InputStream inputStream) throws IOException {
		File tempFile = createTempFile(prefix, suffix);
		LOGGER.log(INFO, "Created temporary file: {0}", tempFile);

		tempFile.deleteOnExit();

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(tempFile);

			copy(inputStream, outputStream);

			return tempFile;
		} finally {
			try {
				outputStream.close();
			} catch (IOException ignore) {
				LOGGER.log(WARNING, "Failed to close OutputStream", ignore);
			}
		}
	}

	private final ReferenceQueue<T> deletionQueue;

	private final Map<Reference<T>, File> tempFileMap;

	public void cleanup(T object, File tempFile) {
		PhantomReference<T> reference = new PhantomReference<T>(object,
				deletionQueue);
		tempFileMap.put(reference, tempFile);
		LOGGER.log(FINE, "Will remove {0} on garbage collection of {1}",
				new Object[] { tempFile, object });
	}

	public void start() {
		DeleteFileRunnable<T> runnable = new DeleteFileRunnable<T>(
				deletionQueue, tempFileMap);
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}

	private static class DeleteFileRunnable<T> implements Runnable {

		public DeleteFileRunnable(ReferenceQueue<T> queue,
				Map<Reference<T>, File> fileMap) {
			this.queue = queue;
			this.fileMap = fileMap;
		}

		private final ReferenceQueue<T> queue;

		private final Map<Reference<T>, File> fileMap;

		@Override
		public void run() {
			try {
				while (true) {
					Reference<? extends T> reference = queue.remove();

					File file = fileMap.get(reference);

					if (file == null) {
						LOGGER.log(WARNING,
								"No cleanup requested for reference!",
								reference);
					} else if (!file.exists()) {
						LOGGER.log(INFO, "\"{0}\" already removed.", file);
					} else if (file.delete()) {
						LOGGER.log(INFO, "Deleted \"{0}\".", file);
					} else if (file.delete()) {
						// Stranger things have happened...
						LOGGER.log(INFO, "Deleted \"{0}\" on second attempt.",
								file);
					} else {
						LOGGER.log(WARNING, "Failed to delete \"{0}\".  "
								+ "Will attempt to delete on exit.", file);
						file.deleteOnExit();
					}
				}
			} catch (InterruptedException ex) {
				LOGGER.log(FINE, "Exiting due to interruption.", ex);
			}
		}

	}

}
