package edu.tju.scs;

import java.util.Arrays;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 10:56 18/5/14.
 */
public class RpcRequest {

    private int requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public RpcRequest() {
    }

    public RpcRequest(int requestId, String methodName, String className, Object[] parameters, Class<?>[] parameterTypes) {
        this.requestId = requestId;
        this.methodName = methodName;
        this.className = className;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcRequest{");
        sb.append("requestId=").append(requestId);
        sb.append(", className='").append(className).append('\'');
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", parameterTypes=").append(parameterTypes == null ? "null" : Arrays.asList(parameterTypes).toString());
        sb.append(", parameters=").append(parameters == null ? "null" : Arrays.asList(parameters).toString());
        sb.append('}');
        return sb.toString();
    }
}
