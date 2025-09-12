/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.structs;

/**
 *
 * @author fredy
 */
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;
import com.itm.ts.ouch.consts.OUCHConsts;

public class OUCHMsgOrderRejected extends OUCHMsgBase {

    private long timestamp;
    private long orderToken;
    private long orderId;
    private int rejectCode;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(long orderToken) {
        this.orderToken = orderToken;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(int rejectCode) {
        this.rejectCode = rejectCode;
    }
    
    public String getRejectDesc() {
//        String zOut;
//        switch (rejectCode) {
//            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
//                zOut = "(" + rejectCode + ")reason: Illegal transaction at this time";
//                break;
//            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING:
//                zOut = "(" + rejectCode + ")reason: Throttling limit exceeded";
//                break;
//            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
//                zOut = "(" + rejectCode + ")reason: Limit orders are not allowed in this session state";
//                break;
//            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_PREMIUM:
//                zOut = "(" + rejectCode + ")reason: The premium must be aligned at the price ticks for the given instrument";
//                break;
//            default:
//                zOut = "(" + rejectCode + ")reason: default_unknown";
//                break;
//        }
//        return zOut;
        String zOut;
        switch (rejectCode) {
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_ILL_TRT_FOR_USER:
                zOut = "(" + rejectCode + ")reason: Transaction is disallowed for this user";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                zOut = "(" + rejectCode + ")reason: The order book is closed";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU:
                zOut = "(" + rejectCode + ")reason: Actor is not allowed to act in this market segment";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_CLI_NOTFOU:
                zOut = "(" + rejectCode + ")reason: The participant is not allowed to act in this market segment";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_USER_NOT_ACTIVE:
                zOut = "(" + rejectCode + ")reason: Actor not in active state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_PART_NOT_ACTIVE:
                zOut = "(" + rejectCode + ")reason: Participant not in active state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_NOT_TRADED:
                zOut = "(" + rejectCode + ")reason: Not allowed to trade in order book";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_OBL_NOTFOU:
                zOut = "(" + rejectCode + ")reason: Specified member does not exist in the member obligation table";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_TRC_INS_NOTFOUND:
                zOut = "(" + rejectCode + ")reason: Trading report class not valid for the instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_TRC_SST_NOTFOUND:
                zOut = "(" + rejectCode + ")reason: Trading report class not valid for session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_TRC_UST_NOTFOUND:
                zOut = "(" + rejectCode + ")reason: Trading report class not valid for the actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU_BID:
                zOut = "(" + rejectCode + ")reason: Actor is not allowed to place bid ordersin this market segment";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU_ASK:
                zOut = "(" + rejectCode + ")reason: Actor is not allowed to place ask ordersin this market segment";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_DUPLICATE_TOKEN:
                zOut = "(" + rejectCode + ")reason: The token is not unique";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_UNKNOWN_TOKEN:
                zOut = "(" + rejectCode + ")reason: The token is not known";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_INVALID_ORDERBOOK:
                zOut = "(" + rejectCode + ")reason: Invalid orderbook";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_INVALID_SIDE:
                zOut = "(" + rejectCode + ")reason: Invalid side";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_INVALID_TIF:
                zOut = "(" + rejectCode + ")reason: Invalid Time In Force";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_CAN_NOT_CANCEL:
                zOut = "(" + rejectCode + ")reason: The order can not be cancelled";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING:
                zOut = "(" + rejectCode + ")reason: Throttling limit exceeded";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MARKET_MAKER_PROTECTION:
                zOut = "(" + rejectCode + ")reason: Market Maker Protection triggered on underlying";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_BID_ASK:
                zOut = "(" + rejectCode + ")reason: Order must specify bid or ask";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_VALIDITY:
                zOut = "(" + rejectCode + ")reason: Given time validity is not allowed";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MARBOUNCE:
                zOut = "(" + rejectCode + ")reason: Market-price orders must be of type Fill or Kill or Immediate or Cancel in this trading state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_PREMIUM:
                zOut = "(" + rejectCode + ")reason: Given premium is not allowed";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_QUANTITY:
                zOut = "(" + rejectCode + ")reason: Illegal quantity in order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ILL_ORDER_TYPE:
                zOut = "(" + rejectCode + ")reason: Unknown order type";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MKT_ORDER_PRICE:
                zOut = "(" + rejectCode + ")reason: A market order must not specify a price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_BLOCK_MAX_LEGS:
                zOut = "(" + rejectCode + ")reason: Too many legs for a block order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_BLOCK_SERIES:
                zOut = "(" + rejectCode + ")reason: Series appears twice on the same side in a block order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MEM_CRS_NOT_ALLOWED:
                zOut = "(" + rejectCode + ")reason: Crossing your own orders is not allowed in this instrument type";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_PRICE_LIMIT:
                zOut = "(" + rejectCode + ")reason: The price is outside the allowed price limits for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_STP_COND:
                zOut = "(" + rejectCode + ")reason: The given stop condition is invalid";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_HIDDEN:
                zOut = "(" + rejectCode + ")reason: Hidden volume is not allowed for this order type";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_SHOWN:
                zOut = "(" + rejectCode + ")reason: The allowed ratio between shown quantity and total quantity has been exceeded";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_NOT_AUTH:
                zOut = "(" + rejectCode + ")reason: The actor is not authorized to do on-behalf transactions";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SHOWN_TOO_SMALL:
                zOut = "(" + rejectCode + ")reason: Shown volume too small";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_NO_WILD_CARD:
                zOut = "(" + rejectCode + ")reason: Wildcards ( * and % ) and spaces are not allowed in the account";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_OP_CLS_REQ:
                zOut = "(" + rejectCode + ")reason: Invalid value in open_close_request";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ORD_NOT_FOU:
                zOut = "(" + rejectCode + ")reason: The given order was not found in the order book";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_FILL_OR_KILL:
                zOut = "(" + rejectCode + ")reason: Fill or kill orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_FILL_OR_KILL:
                zOut = "(" + rejectCode + ")reason: Fill or kill orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_FILL_OR_KILL:
                zOut = "(" + rejectCode + ")reason: Fill or kill orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_IMMEDIATE_OR_CANCEL:
                zOut = "(" + rejectCode + ")reason: Immediate or Cancel orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_IMMEDIATE_OR_CANCEL:
                zOut = "(" + rejectCode + ")reason: Immediate or Cancel orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_IMMEDIATE_OR_CANCEL:
                zOut = "(" + rejectCode + ")reason: Immediate or Cancel orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_FILL_AND_STORE:
                zOut = "(" + rejectCode + ")reason: Fill and store orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_FILL_AND_STORE:
                zOut = "(" + rejectCode + ")reason: Fill and store orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_FILL_AND_STORE:
                zOut = "(" + rejectCode + ")reason: Fill and store orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_LIMIT_ORD:
                zOut = "(" + rejectCode + ")reason: Limit orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                zOut = "(" + rejectCode + ")reason: Limit orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_LIMIT_ORD:
                zOut = "(" + rejectCode + ")reason: Limit orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_MARKET_ORD:
                zOut = "(" + rejectCode + ")reason: Market orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_MARKET_ORD:
                zOut = "(" + rejectCode + ")reason: Market orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_MARKET_ORD:
                zOut = "(" + rejectCode + ")reason: Market orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_ALL_OR_NONE:
                zOut = "(" + rejectCode + ")reason: All or none orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_ALL_OR_NONE:
                zOut = "(" + rejectCode + ")reason: All or none orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_ALL_OR_NONE:
                zOut = "(" + rejectCode + ")reason: All or none orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ILL_ORDER_TYPE_INT:
                zOut = "(" + rejectCode + ")reason: Illegal order type for this instrument type";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_IMBALANCE:
                zOut = "(" + rejectCode + ")reason: Imbalance order is not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_IMBALANCE:
                zOut = "(" + rejectCode + ")reason: Imbalance order is not allowed in current trading state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_IMBALANCE:
                zOut = "(" + rejectCode + ")reason: Imbalance order is not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_MTL_ROUND_LOT:
                zOut = "(" + rejectCode + ")reason: Market to limit is not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_MTL_ROUND_LOT:
                zOut = "(" + rejectCode + ")reason: Market to limit is not allowed in current trading state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_MTL_ROUND_LOT:
                zOut = "(" + rejectCode + ")reason: Market to limit is not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_HIDDEN_AGGRESSIVE:
                zOut = "(" + rejectCode + ")reason: Hidden volume order is not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_HIDDEN_AGGRESSIVE:
                zOut = "(" + rejectCode + ")reason: Hidden volume order is not allowed in current trading state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_HIDDEN_AGGRESSIVE:
                zOut = "(" + rejectCode + ")reason: Hidden volume order is not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MIN_BLK_SIZE:
                zOut = "(" + rejectCode + ")reason: Quantity is less than the minimum for the block size";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MAX_BLK_SIZE:
                zOut = "(" + rejectCode + ")reason: Quantity exceeds the maximum for the block size";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_SHOWN_NOHIDD:
                zOut = "(" + rejectCode + ")reason: Volume may not be decreased";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_SHOWN:
                zOut = "(" + rejectCode + ")reason: Shown volume may not be decreased when hidden volume exists";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_HIDD:
                zOut = "(" + rejectCode + ")reason: Hidden volume may not be decreased";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_INCR_SHOWN:
                zOut = "(" + rejectCode + ")reason: Shown volume may not be increased";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_INCR_HIDD:
                zOut = "(" + rejectCode + ")reason: Hidden volume may not be increased";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_CLIENT:
                zOut = "(" + rejectCode + ")reason: Account (client) field may not be changed";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_PRICE_IMPR:
                zOut = "(" + rejectCode + ")reason: The price may not be improved";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_PRICE_DISIMPR:
                zOut = "(" + rejectCode + ")reason: The price may not be disimproved";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_TIME_EXT:
                zOut = "(" + rejectCode + ")reason: The time validity may not be extended";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_TIME_DECR:
                zOut = "(" + rejectCode + ")reason: The time validity may not be decreased";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_EXCH_ORDER_TYPE:
                zOut = "(" + rejectCode + ")reason: The exchange specific order type may not be changed";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_SHOWN_NOHIDD:
                zOut = "(" + rejectCode + ")reason: Volume may not be decreased in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_SHOWN:
                zOut = "(" + rejectCode + ")reason: Shown volume may not be decreased when hidden volume exists in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_HIDD:
                zOut = "(" + rejectCode + ")reason: Hidden volume may not be decreased in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_INCR_SHOWN:
                zOut = "(" + rejectCode + ")reason: Shown volume may not be increased in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_INCR_HIDD:
                zOut = "(" + rejectCode + ")reason: Hidden volume may not be increased in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_CLIENT:
                zOut = "(" + rejectCode + ")reason: Account (client) field may not be changed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_PRICE_IMPR:
                zOut = "(" + rejectCode + ")reason: The price may not be improved in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_PRICE_DISIMPR:
                zOut = "(" + rejectCode + ")reason: The price may not be disimproved in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_TIME_EXT:
                zOut = "(" + rejectCode + ")reason: The time validity may not be extended in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_TIME_DECR:
                zOut = "(" + rejectCode + ")reason: The time validity may not be decreased in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_PST_ORDER:
                zOut = "(" + rejectCode + ")reason: Price stabilization not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_PST_ORDER:
                zOut = "(" + rejectCode + ")reason: Price stabilization not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_PST_ORDER:
                zOut = "(" + rejectCode + ")reason: Price stabilization not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_SHORT_ORDER:
                zOut = "(" + rejectCode + ")reason: Short Sell not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_SHORT_ORDER:
                zOut = "(" + rejectCode + ")reason: Short Sell not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_SHORT_ORDER:
                zOut = "(" + rejectCode + ")reason: Short Sell not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_MB_ORDER:
                zOut = "(" + rejectCode + ")reason: Market Bid not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_MB_ORDER:
                zOut = "(" + rejectCode + ")reason: Market Bid not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_MB_ORDER:
                zOut = "(" + rejectCode + ")reason: Market Bid not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_BL_ORDER:
                zOut = "(" + rejectCode + ")reason: Best Limit not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_BL_ORDER:
                zOut = "(" + rejectCode + ")reason: Best Limit not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_BL_ORDER:
                zOut = "(" + rejectCode + ")reason: Best Limit not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MB_SELL:
                zOut = "(" + rejectCode + ")reason: Market Bid sell order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MB_ORDER_TYPE:
                zOut = "(" + rejectCode + ")reason: Illegal order type for market bid order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SH_BUY:
                zOut = "(" + rejectCode + ")reason: Short Sell buy order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_FOK_IOC_NOT_ALLOWD:
                zOut = "(" + rejectCode + ")reason: Entered order type is not allowed to have IoC or FoK";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_BEST_LIMIT_REQ:
                zOut = "(" + rejectCode + ")reason: No current market to establish default price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_ALWAYS_INACTIVE:
                zOut = "(" + rejectCode + ")reason: Not possible to perform actions on the type of order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_STOP_PREMIUM:
                zOut = "(" + rejectCode + ")reason: Given stop premium is invalid";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_SSO:
                zOut = "(" + rejectCode + ")reason: State Type Order is not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_SSO:
                zOut = "(" + rejectCode + ")reason: State Type Order is not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_SSO:
                zOut = "(" + rejectCode + ")reason: State Type Order is not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SSO_SPEC_NO_TYPE:
                zOut = "(" + rejectCode + ")reason: State Type Order must specify state type";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_EXT_T_NOT_VALID:
                zOut = "(" + rejectCode + ")reason: State Type valid only for State Type Orders";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_STOP_ORD:
                zOut = "(" + rejectCode + ")reason: STOP orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_STOP_ORD:
                zOut = "(" + rejectCode + ")reason: Stop orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_STOP_ORD:
                zOut = "(" + rejectCode + ")reason: Stop orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INS_GTS:
                zOut = "(" + rejectCode + ")reason: Good till session orders are not allowed for this instrument";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_GTS:
                zOut = "(" + rejectCode + ")reason: Good till session orders are not allowed in this session state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_USR_GTS:
                zOut = "(" + rejectCode + ")reason: Good till session orders are not allowed for this actor";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_MUST_BE_ACTIVE:
                zOut = "(" + rejectCode + ")reason: Order must be specified as active";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ATR_PST_ORDER:
                zOut = "(" + rejectCode + ")reason: Price stabilization not allowed with these order attributes";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IMBALANCE_IOC:
                zOut = "(" + rejectCode + ")reason: Imbalance orders must be of type Immediate or Cancel";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_FOK_NOT_ALLOWD:
                zOut = "(" + rejectCode + ")reason: Entered order type is not allowed to be FoK";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_UPDATE_TO_FOK_IOC:
                zOut = "(" + rejectCode + ")reason: It is not allowed to update this order to IoC or FoK";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_FIXED_PRICE_SESSION:
                zOut = "(" + rejectCode + ")reason: Given price is not allowed in session with fixed price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INV_QUOTE_ITEMS:
                zOut = "(" + rejectCode + ")reason: The number of items is invalid";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ACCOUNT_SUSPENDED:
                zOut = "(" + rejectCode + ")reason: The account is suspended";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_INVESTOR_SUSPENDED:
                zOut = "(" + rejectCode + ")reason: The investor is suspended";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ILL_QUANTITY_RESTRICTION_CHANGE:
                zOut = "(" + rejectCode + ")reason: The quantity restriction of an order is not allowed to change";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_RESERVE_ORDER_QUANTITY_RESTRICTION:
                zOut = "(" + rejectCode + ")reason: An AoN or Minimum fill order is not allowed to be a reserve order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_ILL_RESERVE_CHANGE:
                zOut = "(" + rejectCode + ")reason: The reserve condition of an order is not allowed to change";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_QUANTITY_RESTRICTION_NOT_ALLOWED:
                zOut = "(" + rejectCode + ")reason: Entered order type is not allowed to have quantity restriction";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IMB_TRIGGER_ORDER:
                zOut = "(" + rejectCode + ")reason: Imbalance order is not allowed to be a trigger order";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_OB_NOT_SERIES:
                zOut = "(" + rejectCode + ")reason: The given series has not been defined (in this instance)";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_OB_NOT_CUSTOMER:
                zOut = "(" + rejectCode + ")reason: The given customer was not found or is not in a valid trade state";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IDX_INV_EXCHANGE_INFO:
                zOut = "(" + rejectCode + ")reason: Wrong contents of exchange info";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IDX_INV_PRICE_PST:
                zOut = "(" + rejectCode + ")reason: Price stabilization is not allowed at this price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IDX_SH_ONE_LESS_TICK_ZERO:
                zOut = "(" + rejectCode + ")reason: Price must be greater than or equal to one tick below Last Match Price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IDX_SH_ONE_LESS_TICK_PLUS:
                zOut = "(" + rejectCode + ")reason: Price must be greater than one tick below Last Match Price";
                break;
            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_IDX_MAX_ORDER_QUANTITY_PERCENTAGE_OF_TRADABLE_QUANTITY:
                zOut = "(" + rejectCode + ")reason: Quantity exceeds the max order quantity percentage of tradable quantity";
                break;
            default:
                zOut = "(" + rejectCode + ")reason: default_unknown";
                break;
        }
        return zOut;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 29 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 8 + 4 = 29
                    setTimestamp(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setRejectCode(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgOrderRejected");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
                //.base:
                .concatenateField(getType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 0, 1)
                
                .concatenateField(getTimestamp(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8)
                .concatenateField(getOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getRejectCode(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4)
                
                .putPacketType(ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType.PACKETTYPE_UNSEQUENCED_DATA_PACKET) //.last set before set packet length;
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
