/*
 * Copyright 2013 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.blackbananacoin.ext.curconvert;

import java.util.Date;

public class TwdBit {

	private double twdPerUsd = 30.0d;
	private double usdPerBtc = 100.0d;
	private double twdMinTxFee = 1.0d;

	private Date updateTime;
	private long updateTimeMs;

	public double getTwdPerUsd() {
		return twdPerUsd;
	}

	public void setTwdPerUsd(double twdPerUsd) {
		this.twdPerUsd = twdPerUsd;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public double getUsdPerBtc() {
		return usdPerBtc;
	}

	public void setUsdPerBtc(double usdPerBtc) {
		this.usdPerBtc = usdPerBtc;
	}

	public void update() {
		this.setUpdateTime(new Date());
		this.setUpdateTimeMs(this.getUpdateTime().getTime());
		// min tx fee 0.0001 (BTC)
		double twdPerBtc = usdPerBtc * twdPerUsd;
		setTwdMinTxFee(twdPerBtc * 0.0001d);
	}

	public double getTwdMinTxFee() {
		return twdMinTxFee;
	}

	public void setTwdMinTxFee(double twdMinTxFee) {
		this.twdMinTxFee = twdMinTxFee;
	}

	public long getUpdateTimeMs() {
		return updateTimeMs;
	}

	public void setUpdateTimeMs(long updateTimeMs) {
		this.updateTimeMs = updateTimeMs;
	}

}
