package redis.transaction.entity;

import redis.transaction.enums.GameTransactionCommitResult;
import redis.transaction.enums.GameTransactionEntityCause;
import redis.transaction.enums.GameTransactionLockType;
import redis.transaction.exception.GameTransactionException;
import redis.transaction.service.IRGTRedisService;

/**
 * Created by jiangwenping on 16/12/7.
 * 读取实体
 */
public class CommonReadTransactionEnity extends AbstractGameTransactionEntity {

    public CommonReadTransactionEnity(GameTransactionEntityCause cause, String key,
                                      IRGTRedisService redisService) {
        super(cause, key, redisService, GameTransactionLockType.READ);
    }

    @Override
    public void commit() throws GameTransactionException {

    }

    @Override
    public void rollback() throws GameTransactionException {

    }

    @Override
    public GameTransactionCommitResult trycommit()
            throws GameTransactionException {
        return GameTransactionCommitResult.SUCCESS;
    }

}
