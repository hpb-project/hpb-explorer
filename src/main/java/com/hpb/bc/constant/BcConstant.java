package com.hpb.bc.constant;

/**
 * @author ThinkPad
 * 这里全部声明常量
 */
public interface BcConstant {
    int WEB3J_TIMEOUT = 5;
    String SESSION_KEY = "HPBBC_SESSION_KEY";
    String GAS_LIMIT = "gasLimit";
    String GAS_PRICE = "gasPrice";
    String TRADE_VALUE = "value";
    String PAGENUM = "pageNum";
    String PAGESIZE = "pageSize";
    int PAGESIZE_DEFAULT = 10;
    String EMPTY_MIDDLE_BRACKET = "[]";

    String CONTRACT_ERC20_TYPE = "ERC20";


    String CONTRACT_NOT_TYPE = "NOT";

    String CONTRACT_STATUS_ONE = "1";
    String TX_SMART_CONTRACT_TYPE = "S";
    /**
     * 普通交易
     **/
    String TX_COMMON_TYPE = "C";


    /**
     * 智能合约交易
     */
    String SMART_CONTRACT_TX_LIST = "SmartContractTxList";

    /**
     * 普通交易
     **/
    String COMMON_CONTRACT_TX_LIST = "CommonContractTxList";

    String HX_PREFIX = "0X";

    // ADDRESS
    String ADDRESS_IS_EMPTY = "0301_ADDRESS_IS_EMPTY";
    // EMAIL IS EMPTY
    String EMAIL_IS_EMPTY = "0401_EMAIL_IS_EMPTY";
    // EMAIL_IS_INVALITED
    String EMAIL_IS_INVALITED = "0402_EMAIL_IS_INVALITED";


}
