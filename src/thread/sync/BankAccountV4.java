package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV4 implements BankAccount {
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccountV4(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        lock.lock(); // ReentrantLock 이용하여 lock을 걸기
        // 락을 걸면 받드시 unlock을 해야하므로 try finally를 해야함!
        try {
            log("[검증 시작] 출금액 : " + amount + ", 잔고 : " + balance);
            if (balance < amount) {
                log("[검증 실패] 출금액 : " + amount + ", 잔고 : " + balance);
                return false;
            }
            log("[검증 완료] 출금액 : " + amount + ", 잔고 : " + balance);
            sleep(1000); // 출금하는 시간으로 가정
            balance = balance - amount;
            log("[출금 완료] 출금액 : " + amount + ", 변경 잔고 : " + balance);
        } finally {
            lock.unlock();
        }

        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        lock.lock();
        try{
            return balance;
        }finally {
            lock.unlock();
        }
    }
}
