package com.example.RESTful.validate.type;

public enum  ValidateCodeType {
    SMS{
        @Override
        public String getParamNameOnValidate() {
            return "smsCode";
        }
    },
    IMAGE{
        @Override
        public String getParamNameOnValidate() {
            return "imageCode";
        }
    };
    public abstract String getParamNameOnValidate();
}
