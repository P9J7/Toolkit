import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by p9j7 on 2017/10/11.
 * 个人编写的自动将答案填入试卷小工具
 * A、B、C都为docx格式
 * 其中A文件为原试题卷（要求有题号）
 * B文件为答案卷（形如 1.A后跟随换行符)
 * C文件为填入答案之后的试卷(答案替换题号)
 */
public class FillAnswer {
    public static void main(String[] args) throws IOException {
        //存放题号与答案的Map，key为题号，value为题号对应答案
        Map<String, Object> answerMap = new HashMap<>();
        //定义答案卷的路径
        String answerPath = "D:\\FileCopy\\src\\main\\resources\\B.docx";
        InputStream answerStream = new FileInputStream(answerPath);
        //docx格式文件需要用到pio包解析
        XWPFDocument doc = new XWPFDocument(answerStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        //将答案文件读入
        String anserText = extractor.getText();
        //定义的正则匹配规则,匹配形如 1.A加上换行符的格式，且正则匹配分组方法替换
        Pattern pattern = Pattern.compile("(\\d+)\\.\\s([A-F])\n");
        Matcher matcher = pattern.matcher(anserText);
        //暂存答案
        while (matcher.find()) {
            answerMap.put(matcher.group(1), matcher.group(2));
        }
        //定义试卷路径
        InputStream questionPath = new FileInputStream("D:\\FileCopy\\src\\main\\resources\\A.docx");
        XWPFDocument doc_1 = new XWPFDocument(questionPath);
        List<XWPFParagraph> questionPara = doc_1.getParagraphs();
        //使用答案Map填充试卷文件
        replaceInPara(doc_1, answerMap);
        //定义输出文件的路径
        OutputStream os = new FileOutputStream("D:\\FileCopy\\src\\main\\resources\\C.docx");
        doc_1.write(os);
        close(os);
        close(answerStream);
        close(questionPath);
    }
    //选择文件中的某一段
    private static void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInPara(para, params);
        }
    }
    //具体对某一段进行替换
    private static void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        if (matcher(para.getParagraphText()).find()) {
            //runs对象为段的分句数组
            runs = para.getRuns();
            //因为只需要在题目序号位置填充答案，所以只需要获取第一句
            //for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(0);
            String runText = run.toString();
            matcher = matcher(runText);
            if (matcher.find()) {
                while ((matcher = matcher(runText)).find()) {
                    runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                }
                para.removeRun(0);
                para.insertNewRun(0).setText(runText);
            }
            //}
        }
    }
    //单独定义的匹配规则，匹配题目数字序号且定义了分组
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }
    //关闭输入流
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭输出流
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
