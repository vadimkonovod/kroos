/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fouad Almalki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package main.botlib.core;

/**
 * Several configurations that are applied on <code>JTelegramBot</code> instances.
 */
public class TelegramBotConfig
{
	// default values
	public static final int WORKER_THREADS = 5;
	public static final int POLLING_TIMEOUT_IN_SECONDS = 120;
	
	private int workerThreads = WORKER_THREADS;
	private int pollingTimeoutInSeconds = POLLING_TIMEOUT_IN_SECONDS;
	
	public TelegramBotConfig(int workerThreads, int pollingTimeoutInSeconds)
	{
		this.workerThreads = workerThreads;
		this.pollingTimeoutInSeconds = pollingTimeoutInSeconds;
	}
	
	public int getWorkerThreads(){return workerThreads;}
	public void setWorkerThreads(int workerThreads){this.workerThreads = workerThreads;}
	
	public int getPollingTimeoutInSeconds(){return pollingTimeoutInSeconds;}
	public void setPollingTimeoutInSeconds(int pollingTimeoutInSeconds){this.pollingTimeoutInSeconds = pollingTimeoutInSeconds;}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		TelegramBotConfig that = (TelegramBotConfig) o;
		
		if(workerThreads != that.workerThreads) return false;
		return pollingTimeoutInSeconds == that.pollingTimeoutInSeconds;
		
	}
	
	@Override
	public int hashCode()
	{
		int result = workerThreads;
		result = 31 * result + pollingTimeoutInSeconds;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "TelegramBotConfig{" +
				"workerThreads=" + workerThreads +
				", pollingTimeoutInSeconds=" + pollingTimeoutInSeconds +
				'}';
	}
	
	public static class TelegramBotConfigBuilder
	{
		private int workerThreads = WORKER_THREADS;
		private int pollingTimeoutInSeconds = POLLING_TIMEOUT_IN_SECONDS;
		
		public TelegramBotConfigBuilder(){}
		
		public TelegramBotConfigBuilder workerThreads(int threadsCount)
		{
			this.workerThreads = threadsCount;
			return this;
		}
		
		public TelegramBotConfigBuilder pollingTimeoutInSeconds(int seconds)
		{
			this.pollingTimeoutInSeconds = seconds;
			return this;
		}
		
		public TelegramBotConfig build()
		{
			return new TelegramBotConfig(workerThreads, pollingTimeoutInSeconds);
		}
	}
}