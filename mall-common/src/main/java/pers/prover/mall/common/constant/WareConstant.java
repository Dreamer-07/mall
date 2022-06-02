package pers.prover.mall.common.constant;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/2 15:00
 */
public class WareConstant {

    public enum PurchaseStatusEnum {
        CREATE(0,"新建"),
        ASSIGNED(1, "已分配"),
        PURCHASING(2, "已领取"),
        COMPLETED(3, "已完成"),
        FAILED(4, "有异常");

        private int code;
        private String msg;

        PurchaseStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode(){
            return code;
        }

        public String getMsg(){
            return msg;
        }
    }

    public enum PurchaseDetailStatusEnum {
        CREATE(0,"新建"),
        ASSIGNED(1, "已分配"),
        PURCHASING(2, "正在采购"),
        COMPLETED(3, "已完成"),
        FAILED(4, "采购失败");

        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode(){
            return code;
        }

        public String getMsg(){
            return msg;
        }
    }
}
