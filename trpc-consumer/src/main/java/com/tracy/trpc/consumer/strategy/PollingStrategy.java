package com.tracy.trpc.consumer.strategy;

import com.tracy.trpc.protocol.rpc.RpcCall;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询策略
 *
 * @author tracy.
 * @create 2018-08-16 10:30
 **/
@Slf4j
public class PollingStrategy implements IStrategy {

    private List<RpcCall> rpcCalls;

    private AtomicLong longAdder = new AtomicLong();

    public PollingStrategy(List<RpcCall> rpcCalls) {
        this.rpcCalls = rpcCalls;
    }

    @Override
    public RpcCall select() {
        try {
            long value = longAdder.getAndIncrement();
            return rpcCalls.get((int) (value % rpcCalls.size()));
        } catch (Exception e) {
            log.error("Over size,reset!");
            longAdder = new AtomicLong();
            return rpcCalls.get(0);
        }
    }
}
