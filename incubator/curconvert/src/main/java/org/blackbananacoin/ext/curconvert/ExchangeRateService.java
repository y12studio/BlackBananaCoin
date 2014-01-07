package org.blackbananacoin.ext.curconvert;

import org.blackbananacoin.ext.curconvert.ExtRateGenServiceImpl.MyCallback;

public interface ExchangeRateService {

	void startRequest(MyCallback mcb) throws Exception;

}
