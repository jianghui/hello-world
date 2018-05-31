package communication.rmi;

import java.rmi.RemoteException;   
import java.rmi.server.UnicastRemoteObject;   
  
/**  
* User: staratsky  
* Date: 2008-8-7 21:56:47  
* Զ�̵Ľӿڵ�ʵ��  
*/  
public class HelloRMIImpl extends UnicastRemoteObject implements HelloRMI {   
    private static final long serialVersionUID = -5464145481720553926L;   
  
    /**  
     * ��ΪUnicastRemoteObject�Ĺ��췽���׳���RemoteException�쳣���������Ĭ�ϵĹ��췽������д�����������׳�RemoteException

�쳣  
     *  
     * @throws RemoteException  
     */  
    public HelloRMIImpl() throws RemoteException {   
           
    }   
  
    /**  
     * �򵥵ķ��ء�Hello World��"����  
     *  
     * @return ���ء�Hello World��"����  
     * @throws RemoteException
     */  
    public String helloWorld() throws RemoteException {   
        return "Hello World!";   
    }   
  
    /**  
     * @param someBodyName 
     * @return �����ʺ���  
     * @throws RemoteException
     */  
    public String sayHelloToSomeBody(String someBodyName) throws RemoteException {   
        return "��ã�" + someBodyName + "!";   
    }   
} 