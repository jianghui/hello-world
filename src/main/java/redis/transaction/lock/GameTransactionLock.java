package redis.transaction.lock;

import org.slf4j.Logger;
import redis.transaction.enums.GameTransactionEntityCause;
import redis.transaction.enums.GameTransactionLockStateEnum;
import redis.transaction.exception.GameTransactionException;
import redis.transaction.service.IRGTRedisService;
import redis.transaction.util.Loggers;
import redis.transaction.util.StringUtils;
import redis.transaction.util.TimeUtil;

/**
 * Created by jiangwenping on 16/11/26
 * 写锁
 */
public class GameTransactionLock implements GameTransactionLockInterface {

    protected static Logger logger = Loggers.lockLogger;

    public GameTransactionLock(String lockKey, IRGTRedisService redisService, GameTransactionEntityCause gameTransactionEntityCause) {
        super();
        this.lockKey = lockKey;
        this.redisService = redisService;
        this.gameTransactionEntityCause = gameTransactionEntityCause;
        this.lockState = GameTransactionLockStateEnum.init;
        this.lockTime = TimeUtil.MINUTE_SECOND;
    }

    public GameTransactionLock(String lockKey, IRGTRedisService redisService, GameTransactionEntityCause gameTransactionEntityCause,long lockTime, boolean forceFlag) {
        super();
        this.lockKey = lockKey;
        this.redisService = redisService;
        this.gameTransactionEntityCause = gameTransactionEntityCause;
        this.lockState = GameTransactionLockStateEnum.init;
        this.lockTime = lockTime;
        this.forceFlag = forceFlag;
    }

    public GameTransactionLock(String lockKey, IRGTRedisService redisService, GameTransactionEntityCause gameTransactionEntityCause,long lockTime, boolean forceFlag, String lockContent) {
        super();
        this.lockKey = lockKey;
        this.redisService = redisService;
        this.gameTransactionEntityCause = gameTransactionEntityCause;
        this.lockState = GameTransactionLockStateEnum.init;
        this.lockTime = lockTime;
        this.forceFlag = forceFlag;
        this.lockContent = lockContent;
    }


    /** 对应的锁key*/
    private String lockKey;

    /** redis*/
    private IRGTRedisService redisService;

    private GameTransactionEntityCause gameTransactionEntityCause;

    /**锁定状态 */
    private GameTransactionLockStateEnum lockState;

    /**锁时间 单位为秒*/
    private long lockTime;

    /** 强制标志*/
    private boolean forceFlag;

    /**
     * 锁内容
     */
    private String lockContent="";

    @Override
    public void destroy() {
        //初始化状态不需要销毁
        if(this.lockState.equals(GameTransactionLockStateEnum.init)
                || this.lockState.equals(GameTransactionLockStateEnum.create)){
            return;
        }

        boolean deleteFlag = true;
        if(!StringUtils.isEmptyString(lockContent)){
            deleteFlag = checkContent();
        }

        if(deleteFlag){
            boolean flag = redisService.deleteKey(getLockKey(lockKey, gameTransactionEntityCause));
            if(!flag){
                logger.info("GameTransactionLock" + lockKey + ":gameTransactionEntityCause" + gameTransactionEntityCause.getCause() +  "destroy is error");
            }
        }
    }

    @Override
    public boolean create(long seconds)  throws GameTransactionException {
        this.lockState = GameTransactionLockStateEnum.create;
        boolean createFlag = false;

        try {

            String realLockKey = getLockKey(lockKey, gameTransactionEntityCause);
            logger.info("realLockKey:" + realLockKey);
            createFlag = redisService.setNxString(realLockKey, lockContent, getLockTime());
            if(createFlag){
                logger.info("create realLockKey:" + realLockKey + ",time: " + getLockTime());
                this.lockState = GameTransactionLockStateEnum.success;
                redisService.expire(realLockKey, getLockTime());
            }else{
                if(forceFlag){
                    this.lockState =  GameTransactionLockStateEnum.success;
                    redisService.setString(realLockKey, lockContent, getLockTime());
                    redisService.expire(realLockKey, getLockTime());
                    createFlag = true;
                    logger.info("forece create realLockKey:" + realLockKey  + ",time: " + getLockTime());
                }else{
                    logger.info("create error realLockKey:" + realLockKey  + ",time: " + getLockTime());
                }
            }
        } catch (Exception e) {
            throw new GameTransactionException(e.toString());
        }

        return createFlag;
    }

    /**
     * 获取信息
     * @return
     */
    public String getInfo(){
        return lockKey + StringUtils.DEFAULT_SPLIT + gameTransactionEntityCause.getCause() + StringUtils.DEFAULT_SPLIT + this.lockState;
    }

    /**
     * 获取锁可以
     * @param lockKey
     * @param GameTransactionEntityCause
     * @return
     */
    public String getLockKey(String lockKey, GameTransactionEntityCause GameTransactionEntityCause){
        return lockKey + "#" + gameTransactionEntityCause.getCause();
    }

    /**
     * 获取锁定时间
     * @return
     */
    public int getLockTime(){
        return (int) this.lockTime;
    }

    @Override
    public void setContent(String lockContent) {
        this.lockContent = lockContent;
    }

    /**
     * 检查锁内容
     * @return
     */
    public boolean checkContent(){
        boolean checkFlag = false;
        String content = redisService.getString(getLockKey(lockKey, gameTransactionEntityCause));
        if(!StringUtils.isEmptyString(content)){
            checkFlag = content.equals(this.lockContent );
        }

        return checkFlag;
    }

}

