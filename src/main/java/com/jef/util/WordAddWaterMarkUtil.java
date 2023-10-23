package com.jef.util;

import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.vml.CTGroup;
import com.microsoft.schemas.vml.CTShape;
import com.microsoft.schemas.vml.CTShapetype;
import com.microsoft.schemas.vml.CTTextPath;
import com.microsoft.schemas.vml.STExt;
import com.microsoft.schemas.vml.STTrueFalse;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr.Enum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * 文档加水印
 *
 * @author Administrator
 * @date 2022/5/17
 */
public class WordAddWaterMarkUtil {


    /**
     * 水印参数
     */
    private static final String fontColor = "#D3D3D3"; // 字体颜色

    private static final String fontName = "宋体"; // word字体
    private static final String fontSize = "0.5pt"; // 字体大小
    private static final int widthPerWord = 10; // 一个字平均长度，单位pt，用于：计算文本占用的长度（文本总个数*单字长度）
    private static final String styleRotation = "-45"; // 文本旋转角度

    public static void main(String[] args) throws IOException {
        InputStream in = FileUtil.getInputStreamByUrl("文档地址");
//        InputStream in = new FileInputStream("E:\\Desktop\\新建打印测试文档001.docx");
        XWPFDocument doc = new XWPFDocument(in);
        makeWaterMarkByWord(doc, "天天向上");
        // 文件的后缀名
        String fileType = "docx";
        String saveFileName = REIDIdentifier.randomEOID() + "测试合同." + fileType;
        File saveFile = new File("E:\\download", saveFileName);
        FileOutputStream fopts = new FileOutputStream(saveFile);
        doc.write(fopts);
        fopts.close();

    }

    /**
     * 加上水印(单个)
     *
     * @param docx       XWPFDocument对象
     * @param customText 水印文字
     */
    public static void makeWaterMarkByWord(XWPFDocument docx, String customText) {
        String styleTop = "0pt";  // 与顶部的间距

        if (docx == null) {
            return;
        }
        // 添加水印
        waterMarkDocXDocument(docx, customText, styleTop, 2);
    }

    /**
     * 将指定的字符串重复repeats次.
     *
     * @param pattern 字符串
     * @param repeats 重复次数
     * @return 生成的字符串
     */
    private static String repeatString(String pattern, int repeats) {
        StringBuilder buffer = new StringBuilder(pattern.length() * repeats);
        Stream.generate(() -> pattern).limit(repeats).forEach(buffer::append);
        return new String(buffer);
    }

    /**
     * 为文档添加水印
     * 实现参考了{@link org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy#getWatermarkParagraph(String, int)}
     *
     * @param doc        需要被处理的docx文档对象
     * @param customText 水印文本
     * @param type       类型：1.平铺；2.单个
     */
    private static void waterMarkDocXDocument(XWPFDocument doc, String customText, String styleTop, int type) {
        // poi 1.6写法
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT);
       /* XWPFHeader header;
        try {
            header = createHeader(doc);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }*/
        int size = header.getParagraphs().size();
        if (size == 0) {
            header.createParagraph();
        }
        CTP ctp = header.getParagraphArray(0).getCTP();
        byte[] rsidr = doc.getDocument().getBody().getPArray(0).getRsidR();
        byte[] rsidrdefault = doc.getDocument().getBody().getPArray(0).getRsidRDefault();
        ctp.setRsidP(rsidr);
        ctp.setRsidRDefault(rsidrdefault);
        CTPPr ppr = ctp.addNewPPr();
        ppr.addNewPStyle().setVal("Header");
        // 开始加水印
        CTR ctr = ctp.addNewR();
        CTRPr ctrpr = ctr.addNewRPr();
        ctrpr.addNewNoProof();
        CTGroup group = CTGroup.Factory.newInstance();
        CTShapetype shapetype = group.addNewShapetype();
        CTTextPath shapeTypeTextPath = shapetype.addNewTextpath();
        shapeTypeTextPath.setOn(STTrueFalse.T);
        shapeTypeTextPath.setFitshape(STTrueFalse.T);
        CTLock lock = shapetype.addNewLock();
        lock.setExt(STExt.VIEW);
        CTShape shape = group.addNewShape();
        shape.setId("PowerPlusWaterMarkObject");
        shape.setSpid("_x0000_s102");
        shape.setType("#_x0000_t136");
        if (type != 2) {
            shape.setStyle(getShapeStyle(customText, styleTop)); // 设置形状样式（旋转，位置，相对路径等参数）
        } else {
            shape.setStyle(getShapeStyle()); // 设置形状样式（旋转，位置，相对路径等参数）
        }
        shape.setFillcolor(fontColor);
        shape.setStroked(STTrueFalse.FALSE); // 字体设置为实心
        CTTextPath shapeTextPath = shape.addNewTextpath(); // 绘制文本的路径
        shapeTextPath.setStyle("font-family:" + fontName + ";font-size:" + fontSize); // 设置文本字体与大小
        shapeTextPath.setString(customText);
        CTPicture pict = ctr.addNewPict();
        pict.set(group);
    }


    /**
     * 构建Shape的样式参数
     *
     * @param customText 水印文本
     * @return
     */
    private static String getShapeStyle(String customText, String styleTop) {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";width: ").append(customText.length() * widthPerWord).append("pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("20pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-top: ").append(styleTop);
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-position-vertical: ").append("left");
        sb.append(";mso-position-horizontal: ").append("center");
        sb.append(";rotation: ").append(styleRotation);
        return sb.toString();
    }

    /**
     * 构建Shape的样式参数
     *
     * @return
     */
    private static String getShapeStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";left: ").append("opt");
        sb.append(";width: ").append("500pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("150pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";margin-left: ").append("-50pt");
        sb.append(";margin-top: ").append("270pt");
        sb.append(";mso-position-horizontal-relative: ").append("margin");
        sb.append(";mso-position-vertical-relative: ").append("margin");
        sb.append(";mso-width-relative: ").append("page");
        sb.append(";mso-height-relative: ").append("page");
        sb.append(";rotation: ").append("-2949120f");
        return sb.toString();
    }

    /**
     * 模拟poi 1.6写法
     *
     * @param doc
     * @return org.apache.poi.xwpf.usermodel.XWPFHeader
     * @author Jef
     * @date 2022/5/19
     */
    public static XWPFHeader createHeader(XWPFDocument doc) throws IOException {
        XWPFHeaderFooterPolicy hfPolicy = doc.createHeaderFooterPolicy();
        return hfPolicy.createHeader(Enum.forInt(2));
    }


}