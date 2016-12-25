package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/25.
 */
public class PacketListEntity {

    /**
     * code : 200
     * message : 成功
     * object : {"amount":4.050000000000001,"count":5,"redPacketOut":[{"allAmount":0.81,"allotPersonNum":10,"createTime":1482404208000,"customerId":53,"id":19,"metalOrderId":"129269098426740736","status":1,"truePersonNum":1,"type":1,"validTime":1482490608000}]}
     * success : true
     */

    private int code;
    private String message;
    private Packets object;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Packets getObject() {
        return object;
    }

    public void setObject(Packets object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Packets {
        /**
         * amount : 4.050000000000001
         * count : 5
         * redPacketOut : [{"allAmount":0.81,"allotPersonNum":10,"createTime":1482404208000,"customerId":53,"id":19,"metalOrderId":"129269098426740736","status":1,"truePersonNum":1,"type":1,"validTime":1482490608000}]
         */

        private double amount;
        private int count;
        private List<PacketDetailEntity> redPacketOut;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<PacketDetailEntity> getRedPacketOut() {
            return redPacketOut;
        }

        public void setRedPacketOut(List<PacketDetailEntity> redPacketOut) {
            this.redPacketOut = redPacketOut;
        }
    }

    @Override
    public String toString() {
        return "PacketListEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", object=" + object +
                ", success=" + success +
                '}';
    }
}
