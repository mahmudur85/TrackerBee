package com.trackertraced.trackerbee.application.utils;

/**
 * Created by Mahmudur on 14-Mar-15.
 */
public final class ConstantsKeyValues {
    public static final class ServerMessageConsttants {
        public static final class MessageTags {
            public static final String TAG_MESSAGE_TYPE = "tag_message_type";

            public static final class GetInstance {
                public static final String TAG_TIME_FROM = "tag_time_from";
                public static final String TAG_TIME_TO = "tag_time_to";
                public static final String TAG_ROWS = "tag_rows";
            }
        }

        public static final class MessageTypes {
            public static final int TYPE_MESSAGE_GET_INSTANCE = 1;
        }
    }
}
