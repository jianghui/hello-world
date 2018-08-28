package redis.transaction.factory;

import redis.transaction.enums.GameTransactionEntityCause;
import redis.transaction.util.GlobalConstants;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class GameTransactionKeyFactory {

    public static String getCommonTransactionEntityKey(GameTransactionEntityCause cause, String RedisKey, String union){
        return RedisKey + cause.getCause() + GlobalConstants.Strings.commonSplitString + union;
    }
}
