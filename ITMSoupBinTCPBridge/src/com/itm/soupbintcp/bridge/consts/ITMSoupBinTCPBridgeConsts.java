/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.consts;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeConsts {
    
    public class SoupBinTCPPacketType {
        
        //.packet types:
        public static final String PACKETTYPE_DEBUG_PACKET                      = "+";
        public static final String PACKETTYPE_LOGIN_ACCEPTED_PACKET             = "A";
        public static final String PACKETTYPE_LOGIN_REJECTED_PACKET             = "J";
        public static final String PACKETTYPE_SEQUENCED_DATA_PACKET             = "S";
        public static final String PACKETTYPE_SERVER_HEARTBEAT_PACKET           = "H";
        public static final String PACKETTYPE_END_OF_SESSION_PACKET             = "Z";
        public static final String PACKETTYPE_LOGIN_REQUEST_PACKET              = "L";
        public static final String PACKETTYPE_UNSEQUENCED_DATA_PACKET           = "U";
        public static final String PACKETTYPE_CLIENT_HEARTBEAT_PACKET           = "R";
        public static final String PACKETTYPE_LOGOUT_REQUEST_PACKET             = "O";
        
    }
    
    public class SoupBinTCPValue {
        
        //.login reject codes:
        public static final String LOGIN_REJECT_CODE_NOT_AUTHORIZED             = "A";
        public static final String LOGIN_REJECT_CODE_SESSION_NOT_AVAILABLE      = "S";
        
        
    }
    
    public class SoupBinTCPLength {
        
        public static final int MINIMUM_PACKET_SIZE                             = 3;
        
        public static final int SIZEOF_FIELD_PACKET_LENGTH                      = 2;
        public static final int SIZEOF_FIELD_PACKET_TYPE                        = 1;
        
        
    }
    
    public class SoupBinTCPOffset {
        
        public static final int OFFSET_FIELD_PACKET_LENGTH                      = 0;
        public static final int OFFSET_FIELD_PACKET_TYPE                        = 2;
        public static final int OFFSET_FIELD_PAYLOAD                            = 3;
        
        
    }
    
    public enum ITCHType{ 
        ITCH                    (0, "Itch"),
        ITCH_MDF                (1, "Itch-MDF");
        
        public final int type;
        public final String cname;

        private ITCHType(int type, String cname) {
            this.type = type;
            this.cname = cname;
        }

    }
}
