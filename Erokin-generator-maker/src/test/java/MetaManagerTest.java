import com.erokin.maker.mata.Meta;
import com.erokin.maker.mata.MetaManager;

public class MetaManagerTest {
    public static void main(String[] args)
    {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta.toString());
    }
}
