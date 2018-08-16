package com.tracy.trpc.protocol.rpc;

import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;

public interface RpcCall {
    ResponseModel invoke(InvokeModel invokeModel) throws Exception;
}
