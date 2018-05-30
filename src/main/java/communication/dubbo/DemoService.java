package communication.dubbo;

import java.io.Serializable;
import java.util.List;

public interface DemoService extends Serializable{

	String sayHello(String name);

	public List getUsers();

}