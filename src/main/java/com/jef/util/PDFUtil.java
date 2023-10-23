package com.jef.util;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/** PDF读写类
 * @author Jef
 * @date 2018/7/24 21:34
 */
public class PDFUtil {

    	public static final String CHARACTOR_FONT_CH_FILE = "SIMFANG.TTF";  //仿宋常规
//    public static final String CHARACTOR_FONT_CH_FILE = "SIMHEI.TTF";  //黑体常规

    public static final Rectangle PAGE_SIZE = PageSize.A4;
    public static final float MARGIN_LEFT = 50;
    public static final float MARGIN_RIGHT = 50;
    public static final float MARGIN_TOP = 50;
    public static final float MARGIN_BOTTOM = 50;
    public static final float SPACING = 20;


    private Document document = null;

    /**
     * 功能：创建导出数据的目标文档
     * @param fileName 存储文件的临时路径
     * @return
     */
    public void createDocument(String fileName) {
        File file = new File(fileName);
        FileOutputStream out = null;
        document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
        try {
            out = new FileOutputStream(file);
//			PdfWriter writer =
            PdfWriter.getInstance(document, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 打开文档准备写入内容
        document.open();
    }

    /**
     * 将章节写入到指定的PDF文档中
     * @param chapter
     * @return
     */
    public void writeChapterToDoc(Chapter chapter) {
        try {
            if(document != null) {
                if(!document.isOpen()) document.open();
                document.add(chapter);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能  创建PDF文档中的章节
     * @param title 章节标题
     * @param chapterNum 章节序列号
     * @param alignment 0表示align=left，1表示align=center
     * @param numberDepth 章节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号
     * @param font 字体格式
     * @return Chapter章节
     */
    public static Chapter createChapter(String title, int chapterNum, int alignment, int numberDepth, Font font) {
        Paragraph chapterTitle = new Paragraph(title, font);
        chapterTitle.setAlignment(alignment);
        Chapter chapter = new Chapter(chapterTitle, chapterNum);
        chapter.setNumberDepth(numberDepth);
        return chapter;
    }

    /**
     * 功能：创建某指定章节下的小节
     * @param chapter 指定章节
     * @param title 小节标题
     * @param font 字体格式
     * @param numberDepth 小节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号
     * @return section在指定章节后追加小节
     */
    public static Section createSection(Chapter chapter, String title, Font font, int numberDepth) {
        Section section = null;
        if(chapter != null) {
            Paragraph sectionTitle = new Paragraph(title, font);
            sectionTitle.setSpacingBefore(SPACING);
            section = chapter.addSection(sectionTitle);
            section.setNumberDepth(numberDepth);
        }
        return section;
    }

    /**
     * 功能：向PDF文档中添加的内容
     * @param text 内容
     * @param font 内容对应的字体
     * @return phrase 指定字体格式的内容
     */
    public static Phrase createPhrase(String text,Font font) {
        Phrase phrase = new Paragraph(text,font);
        return phrase;
    }

    /**
     * 功能：创建列表
     * @param numbered  设置为 true 表明想创建一个进行编号的列表
     * @param lettered 设置为true表示列表采用字母进行编号，为false则用数字进行编号
     * @param symbolIndent
     * @return list
     */
    public static List createList(boolean numbered, boolean lettered, float symbolIndent) {
        List list = new List(numbered, lettered, symbolIndent);
        return list;
    }

    /**
     * 功能：创建列表中的项
     * @param content 列表项中的内容
     * @param font 字体格式
     * @return listItem
     */
    public static ListItem createListItem(String content, Font font) {
        ListItem listItem = new ListItem(content, font);
        return listItem;
    }

    /**
     * 功能：创造字体格式
     * @param fontname
     * @param size 字体大小
     * @param style 字体风格
     * @param color 字体颜色
     * @return Font
     */
    public static Font createFont(String fontname, float size, int style, BaseColor color) {
        Font font =  FontFactory.getFont(fontname, size, style, color);
        return font;
    }

    /**
     * 功能： 返回支持中文的字体---仿宋
     * @param size 字体大小
     * @param style 字体风格
     * @param color 字体 颜色
     * @return  字体格式
     */
    public static Font createCHineseFont(float size, int style, BaseColor color) {
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont(CHARACTOR_FONT_CH_FILE,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Font(bfChinese, size, style, color);
    }

    /**
     * 最后关闭PDF文档
     */
    public void closeDocument() {
        if(document != null) {
            document.close();
        }
    }



    /**
     * 测试pdf文件的创建
     * @param args
     */
    public static void main(String[] args) {

        String fileName = "F:test11.pdf";  //这里先手动把绝对路径的文件夹给补上。
        PDFUtil pdfUtil = new PDFUtil();

        Font chapterFont = PDFUtil.createCHineseFont(20, Font.BOLD, new BaseColor(0, 0, 255));//文章标题字体
        Font sectionFont = PDFUtil.createCHineseFont(16, Font.BOLD, new BaseColor(0, 0, 255));//文章小节字体
        Font textFont = PDFUtil.createCHineseFont(10, Font.NORMAL, new BaseColor(0, 0, 0));//小节内容字体

        pdfUtil.createDocument(fileName);
        Chapter chapter = PDFUtil.createChapter("糖尿病病例1", 1, 1, 0, chapterFont);
        Section section1 = PDFUtil.createSection(chapter, "病例联系人信息", sectionFont,0);
        Phrase text1 = PDFUtil.createPhrase("如您手中有同类现成病例，在填写完以上基础信息后，传病例附件",textFont);
        section1.add(text1);

        Section section2 = PDFUtil.createSection(chapter, "病例个人体会", sectionFont,0);
        Phrase text2 = PDFUtil.createPhrase("1.下载病例生成PDF文档",textFont);
//		text2.setFirstLineIndent(20);  //第一行空格距离
        section2.add(text1);
        section2.add(text2);

        List list = PDFUtil.createList(true, false, 20);
        String tmp = "还有什么能够文档。文档是 PDF 文档的所有元素的容器。 ";
        ListItem listItem1 = PDFUtil.createListItem(tmp,textFont);
        ListItem listItem2 = PDFUtil.createListItem("列表2",textFont);
        list.add(listItem1);
        list.add(listItem2);
        section2.add(list);

        pdfUtil.writeChapterToDoc(chapter);
        pdfUtil.closeDocument();
    }
}
