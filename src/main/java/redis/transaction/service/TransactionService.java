package redis.transaction.service;

import redis.transaction.entity.AbstractGameTransactionEntity;
import redis.transaction.enums.GameTransactionCause;
import redis.transaction.enums.GameTransactionCommitResult;

/**
 * Created by jiangwenping on 16/11/26.
 * 提交事务，允许多个实体
 */
public interface TransactionService {
    /**
     * 提交事务
     * @return
     */
    public GameTransactionCommitResult commitTransaction(GameTransactionCause gameTransactionCause, AbstractGameTransactionEntity... abstractGameTransactionEntity);
    /**
     * 提交事务
     * @return
     */
    public GameTransactionCommitResult commitTransaction(GameTransactionCause gameTransactionCause, long waitTime, AbstractGameTransactionEntity... abstractGameTransactionEntity);
}
