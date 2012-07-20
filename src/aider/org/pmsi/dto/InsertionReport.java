package aider.org.pmsi.dto;

public class InsertionReport {

	private boolean readerSuccess = false,
			        writerSuccess = false,
			        muxerSuccess = false,
			        threadSucess = false,
			        runnerSuccess = false;
	
	private String readerReport = null,
				   writerReport = null,
				   muxerReport = null,
				   threadReport = null,
				   runnerReport = null;

	public synchronized boolean isReaderSuccess() {
		return readerSuccess;
	}

	public synchronized void setReaderSuccess(boolean readerSuccess) {
		this.readerSuccess = readerSuccess;
	}

	public synchronized boolean isWriterSuccess() {
		return writerSuccess;
	}

	public synchronized void setWriterSuccess(boolean writerSuccess) {
		this.writerSuccess = writerSuccess;
	}

	public synchronized boolean isMuxerSuccess() {
		return muxerSuccess;
	}

	public synchronized void setMuxerSuccess(boolean muxerSuccess) {
		this.muxerSuccess = muxerSuccess;
	}

	public synchronized boolean isThreadSucess() {
		return threadSucess;
	}

	public synchronized void setThreadSucess(boolean threadSucess) {
		this.threadSucess = threadSucess;
	}

	public synchronized boolean isRunnerSuccess() {
		return runnerSuccess;
	}

	public synchronized void setRunnerSuccess(boolean runnerSuccess) {
		this.runnerSuccess = runnerSuccess;
	}

	public synchronized String getReaderReport() {
		return readerReport;
	}

	public synchronized void setReaderReport(String readerReport) {
		this.readerReport = readerReport;
	}

	public synchronized String getWriterReport() {
		return writerReport;
	}

	public synchronized void setWriterReport(String writerReport) {
		this.writerReport = writerReport;
	}

	public synchronized String getMuxerReport() {
		return muxerReport;
	}

	public synchronized void setMuxerReport(String muxerReport) {
		this.muxerReport = muxerReport;
	}

	public synchronized String getThreadReport() {
		return threadReport;
	}

	public synchronized void setThreadReport(String threadReport) {
		this.threadReport = threadReport;
	}

	public synchronized String getRunnerReport() {
		return runnerReport;
	}

	public synchronized void setRunnerReport(String runnerReport) {
		this.runnerReport = runnerReport;
	}
}
