package edu.tju.scs;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 11:00 18/5/14.
 */
public class RpcResponse {

    private int responseId;
    private Object result;
    private String error;

    public RpcResponse() {
    }

    public RpcResponse(int responseId, Object result, String error) {
        this.responseId = responseId;
        this.result = result;
        this.error = error;
    }


    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcResponse{");
        sb.append("responseId=").append(responseId);
        sb.append(", result=").append(result);
        sb.append(", error='").append(error).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
