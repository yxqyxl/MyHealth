import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class TestDemo {


    @Test
    public void test() throws Exception{
//      第一步：创建一个Configuration 对象，直接new一个对象。构造方法的参数是freemarker的版本号
        Configuration configuration = new Configuration(Configuration.getVersion());
//      第二步：设置模板文件所在路径
        configuration.setDirectoryForTemplateLoading(new File("E:\\projects\\projectCode\\MyHealth\\health_parent\\health_freemarker_demo\\src\\main\\resources"));
//      第三步：设置模板文件使用的字符集，一般就是utf-8
        configuration.setDefaultEncoding("utf-8");
//      第四步：加载一个模板，创建一个模板对象
        Template template = configuration.getTemplate("hello.ftl");
//      第五步：创建一个模板使用的数据集，可以是pojo  也可以是map，一般是Map
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("message","你是个好人");
        map.put("success","true");
//      第六步：创建一个Writer对象，一般创建  FileWriter对象，指定生成文件命
        FileWriter writer = new FileWriter("E:\\Demo01.html");
//      第七步：调用模板对象process方法输出文件
        template.process(map,writer);
//      第八步：关闭流
        writer.close();
    }


}
