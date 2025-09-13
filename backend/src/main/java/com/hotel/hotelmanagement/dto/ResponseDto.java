package com.hotel.hotelmanagement.dto;

public class ResponseDto {
    private int status;
    private Object body;

    //constructors
    public ResponseDto(int status, Object body){
        this.status=status;
        this.body=body;
    }

    //getter
    public int getStatus(){
        return status;
    }

    public Object getBody() {
        return body;
    }

    //setter
    public void setStatus(int status) {
        this.status = status;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
