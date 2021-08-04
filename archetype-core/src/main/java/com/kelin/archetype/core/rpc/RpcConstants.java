// Copyright 2021 Kelin Inc. All rights reserved.

package com.kelin.archetype.core.rpc;

/**
 * @author Kelin Tan
 */
public class RpcConstants {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final int RETRY_TIMES = 1;
    public static final boolean DEFAULT_ASYNC = false;

    public static final String RPC_NAME_HEADER = "X-RPC-NAME";
}
