import com.erokin.maker.meta.Meta;
import com.erokin.maker.meta.MetaManager;

public class MetaManagerTest {
    public static void main(String[] args)
    {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta.toString());
    }
}
