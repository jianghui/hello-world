package communication.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException; 
public interface HelloRMI extends Remote {   
   
    /**  
     * �򵥵ķ��ء�Hello World��"����  
     * @return ���ء�Hello World��"����  
     * @throws RemoteException
     */  
    public String helloWorld() throws RemoteException;   
  
    /**  
     * @param someBodyName   
     * @return ������Ӧ���ʺ���  
     * @throws RemoteException
     */  
    public String sayHelloToSomeBody(String someBodyName) throws RemoteException;   
       
} 