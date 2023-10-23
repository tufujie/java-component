package com.jef.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.util.List;

/**
 * 利用JACOB对Microsoft Office Word 进行相关操作
 * @author Jef
 * @since 2018-06-05
 */
public class MSOfficeGeneratorUtils {
    /**
     * Microsoft Office Word 程序对象
     */
    private ActiveXComponent word = null;
    /**
     * Word 活动文档对象
     */
    private Dispatch document = null;
    /**
     * 所有 Word 文档对象
     */
    private Dispatch documents = null;
    /**
     * selection 代表当前活动文档窗口中的所选内容。如果文档中没有选中任何内容，则此对象代表插入点（即光标所在位置）。<br/>
     * 每个文档窗口中只能存在一个selection对象，并且在整个应用程序中，只能存在一个活动的selection对象
     */
    private Dispatch selection = null;
    /**
     * range 对象代表文档中的一个连续的区域。每个range对象由一个起始字符位置与结束字符位置定义。<br/>
     * range 对象独立于所选内容。你可以定义和处理一个范围而无需改变所选内容。还可以在文档中定义多个范围。但每个文档中只能有一个所选内容
     */
    private Dispatch range = null;
    /**
     * PageSetup 对象包含文档所有页面的设置属性（如纸张大小，左边距，下边距）
     */
    private Dispatch pageSetup = null;
    /**
     * 文档中的所有表格对象
     */
    private Dispatch tables = null;
    /** 单个表格对象 */
    private Dispatch table = null;
    /** 表格所有行对象 */
    private Dispatch rows = null;
    /** 表格所有列对象 */
    private Dispatch cols = null;
    /** 表格指定行对象 */
    private Dispatch row = null;
    /** 表格指定列对象 */
    private Dispatch col = null;
    /** 表格中指定的单元格 */
    private Dispatch cell = null;
    /** 字体 */
    private Dispatch font = null;
    /** 对齐方式 */
    private Dispatch alignment = null;
    /**
     * 构造方法
     *
     * @param visible
     *            设置在生成word文档时，程序是否可见
     */
    public MSOfficeGeneratorUtils(boolean visible) {
        if (this.word == null) {
            // 初始化Microsoft Office Word 实例
            this.word = new ActiveXComponent("Word.Application");
            this.word.setProperty("Visible", new Variant(visible));
            // 禁用宏
            this.word.setProperty("AutomationSecurity", new Variant(3));
        }
        if (this.documents == null)
            this.documents = word.getProperty("Documents").toDispatch();
    }
    /**
     * 设置页面方向与页边距
     *
     * @param orientation
     *            页面方向
     *            <ul>
     *            <li>0 横向</li>
     *            <li>1 纵向</li>
     *            </ul>
     * @param leftMargin
     *            左边距
     * @param rightMargin
     *            右边距
     * @param topMargin
     *            上边距
     * @param buttomMargin
     *            下边距
     */
    public void setPageSetup(int orientation, int leftMargin, int rightMargin,
                             int topMargin, int buttomMargin) {
        if (this.pageSetup == null)
            this.getPageSetup();
        Dispatch.put(pageSetup, "Orientation", orientation);
        Dispatch.put(pageSetup, "LeftMargin", leftMargin);
        Dispatch.put(pageSetup, "RightMargin", rightMargin);
        Dispatch.put(pageSetup, "TopMargin", topMargin);
        Dispatch.put(pageSetup, "BottomMargin", buttomMargin);

    }

    /**
     * 打开word文档
     *
     * @param docPath
     *            word文档路径
     * @return 打开的文档对象
     */
    public Dispatch openDocument(String docPath) {
        this.document = Dispatch.call(documents, "Open", docPath).toDispatch();
        this.getSelection();
        this.getRange();
        this.getAlignment();
        this.getFont();
        this.getPageSetup();
        return this.document;
    }
    /**
     * 创建一篇新文档
     *
     * @return 文档对象
     */
    public Dispatch createNewDocument() {
        this.document = Dispatch.call(documents, "Add").toDispatch();
        this.getSelection();
        this.getRange();
        this.getPageSetup();
        this.getAlignment();
        this.getFont();
        return this.document;
    }
    /**
     * 获取选定的内容或插入点
     *
     * @return selection
     */
    public Dispatch getSelection() {
        this.selection = word.getProperty("Selection").toDispatch();
        return this.selection;
    }
    /**
     * 获取当前文档中可以修改的部分，前提是必须存在选中内容
     *
     * @return range
     */
    public Dispatch getRange() {
        this.range = Dispatch.get(this.selection, "Range").toDispatch();
        return this.range;
    }
    /**
     * 获得当前文档的页面属性
     */
    public Dispatch getPageSetup() {
        if (this.document == null)
            return this.pageSetup;
        this.pageSetup = Dispatch.get(this.document, "PageSetup").toDispatch();
        return this.pageSetup;
    }
    /**
     * 把选中内容或插入点向上移动
     *
     * @param count
     *            移动的距离
     */
    public void moveUp(int count) {
        for (int i = 0; i < count; i++)
            Dispatch.call(this.selection, "MoveUp");
    }
    /**
     * 把选中内容或插入点向下移动
     *
     * @param count
     *            移动的距离
     */
    public void moveDown(int count) {
        for (int i = 0; i < count; i++)
            Dispatch.call(this.selection, "MoveDown");
    }
    /**
     * 把选中内容或插入点向左移动
     *
     * @param count
     *            移动的距离
     */
    public void moveLeft(int count) {
        for (int i = 0; i < count; i++)
            Dispatch.call(this.selection, "MoveLeft");
    }
    /**
     * 把选中内容或插入点向右移动
     *
     * @param count
     *            移动的距离
     */
    public void moveRight(int count) {
        for (int i = 0; i < count; i++)
            Dispatch.call(this.selection, "MoveRight");
    }
    /**
     * 执行硬换行（回车键）
     *
     * @param count
     *            换行数
     */
    public void enterDown(int count) {
        for (int i = 0; i < count; i++)
            Dispatch.call(this.selection, "TypeParagraph");
    }
    /**
     * 把插入点移动到文件首位置
     */
    public void moveStart() {
        Dispatch.call(this.selection, "HomeKey", new Variant(6));
    }
    /**
     * 把插入点移动到文件末尾
     */
    public void moveEnd() {
        Dispatch.call(selection, "EndKey", new Variant(6));
    }


    /**
     * 从选定内容或插入点开始查找文本
     *
     * @param toFindText
     *            要查找的内容
     * @return 查询到的内容并选中
     */
    public boolean find(String toFindText) {
        // 从selection所在位置开始查询
        Dispatch find = Dispatch.call(this.selection, "Find").toDispatch();
        // 设置要查找的?热?br />
        Dispatch.put(find, "Text", toFindText);
        // 向前查找
        Dispatch.put(find, "Forward", "True");
        // 设置格式
        Dispatch.put(find, "Format", "True");
        // 大小写匹配
        Dispatch.put(find, "MatchCase", "True");
        // 全字匹配
        Dispatch.put(find, "MatchWholeWord", "True");
        // 查找并选中
        return Dispatch.call(find, "Execute").getBoolean();
    }
    /**
     * 替换选定的内容
     *
     * @param newText
     *            要替换的内容
     */
    public void replace(String newText) {
        // 设置替换文本
        Dispatch.put(this.selection, "Text", newText);
    }
    /**
     * 全局替换
     *
     * @param oldText
     *            要替换的内容
     * @param replaceObj
     *            被替换的内容
     */
    public void replaceAll(String oldText, Object replaceObj) {
        // 将插入点移到文件开头
        moveStart();
        // 表格替换方式
        String newText = (String) replaceObj;
        // 图片替换方式
        if (oldText.indexOf("image") != -1 || newText.lastIndexOf(".bmp") != -1 || newText.lastIndexOf(".jpg") != -1 || newText.lastIndexOf(".gif") != -1) {
            while (find(oldText)) {
                insertImage(newText);
                Dispatch.call(this.selection, "MoveRight");
            }
            // 文本方式
        } else {
            while (find(oldText)) {
                replace(newText);
                Dispatch.call(this.selection, "MoveRight");
            }
        }
    }

    /**
     * 将指定的内容替换成图片
     * @param replaceText 指定的内容
     * @param imgPath 图片路径
     */
    public void replaceText2Image(String replaceText,String imgPath){
        moveStart();
        while(find(replaceText)){
            insertImage(imgPath);
            moveEnd();
            // enterDown(1);
        }
    }
    /**
     * 向当前插入点替换图片
     *
     * @param imagePath
     *            图片的路径
     */
    public void insertImage(String imagePath) {
        Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(), "AddPicture", imagePath);
    }
    /**
     * 合并单元格
     *
     * @param tableIndex
     *            表格下标，从1开始
     * @param fstCellRowIdx
     *            开始行
     * @param fstCellColIdx
     *            开始列
     * @param secCellRowIdx
     *            结束行
     * @param secCellColIdx
     *            结束列
     */
    public void mergeCell(int tableIndex, int fstCellRowIdx, int fstCellColIdx,
                          int secCellRowIdx, int secCellColIdx) {
        getTable(tableIndex);
        Dispatch fstCell = Dispatch.call(table, "Cell",
                new Variant(fstCellRowIdx), new Variant(fstCellColIdx))
                .toDispatch();
        Dispatch secCell = Dispatch.call(table, "Cell",
                new Variant(secCellRowIdx), new Variant(secCellColIdx))
                .toDispatch();
        Dispatch.call(fstCell, "Merge", secCell);
    }
    /**
     * 拆分当前单元格
     *
     * @param numRows
     *            拆分的行数，如果不想拆分行，请指定为1
     * @param numColumns
     *            拆分的列数，如果不想拆分列，请指定为1
     */
    public void splitCell(int numRows, int numColumns) {
        Dispatch.call(this.cell, "Split", new Variant(numRows), new Variant(
                numColumns));
    }
    /**
     * 向表格中写入内容
     *
     * @param list
     *            要写入的内容<br/>
     *            注：list.size() 应该与表格的rows一致，String数组的length属性应与表格的columns一致
     */
    public void insertToTable(List<String[]> list) {
        if (list == null || list.size() <= 0)
            return;
        if (this.table == null)
            return;
        for (int i = 0; i < list.size(); i++) {
            String[] strs = list.get(i);
            for (int j = 0; j < strs.length; j++) {
                // 遍历表格中每一??单元格，遍历次数所要填入的?热菔?肯嗤?br />     Dispatch cell = this.getCell(i + 1, j + 1);
                // 选中此单元格
                Dispatch.call(cell, "Select");
                // 写入?热莸酱说ピ?裰?br />     Dispatch.put(this.selection, "Text", strs[j]);
                // 将插入点移动至下一??位置
            }
            this.moveDown(1);
        }
        // 换行
        this.enterDown(1);
    }
    /**
     * 向当前插入点插入文本内容
     *
     * @param list
     *            要插入的内容，list.size()代表行数
     */
    public void insertToDocument(List<String> list) {
        if (list == null || list.size() <= 0)
            return;
        if (this.document == null)
            return;
        for (String str : list) {
            Dispatch.put(this.selection, "Text", str);
            this.moveDown(1);
            this.enterDown(1);
        }
    }
    /**
     * 在当前插入点插入文本
     *
     * @param insertText
     *            要插入的文本
     */
    public void insertToText(String insertText) {
        Dispatch.put(this.selection, "Text", insertText);
    }
    /**
     * 在当前插入点插入字符串,利用此方法插入一行text后，Word会默认选中它，如果再调用此方法，会将原来的内容覆盖掉，所以调用此方法后，记得调用moveRight，将偏移量向右边移动一个位置 。
     * @param newText 要插入的新字符串
     */
    public void insertText(String newText) {
        Dispatch.put(selection, "Text", newText);
    }
    /**
     * 创建新的表格
     *
     * @param rowCount
     *            行
     * @param colCount
     *            列
     * @param width
     *            表格边框
     *            <ul>
     *            <li>0 无边框</li>
     *            <li>1 有边框</li>
     *            </ul>
     * @return 表格对象
     */
    public Dispatch createNewTable(int rowCount, int colCount, int width) {
        if (this.tables == null)
            this.getTables();
        this.getRange();
        if (rowCount > 0 && colCount > 0)
            this.table = Dispatch.call(this.tables, "Add", this.range,
                    new Variant(rowCount), new Variant(colCount),
                    new Variant(width)).toDispatch();
        return this.table;
    }
    /**
     * 获取当前document对象中的所有表格对象
     *
     * @return tables
     */
    public Dispatch getTables() {
        if (this.document == null)
            return this.tables;
        this.tables = Dispatch.get(this.document, "Tables").toDispatch();
        return this.tables;
    }
    /**
     * 获取当前文档中的所有表格数量
     *
     * @return 表格数量
     */
    public int getTablesCount() {
        if (this.tables == null)
            this.getTables();
        return Dispatch.get(tables, "Count").getInt();
    }
    /**
     * 根据索引获得table对象
     *
     * @param tableIndex
     *            索引
     * @return table
     */
    public Dispatch getTable(int tableIndex) {
        if (this.tables == null)
            this.getTables();
        if (tableIndex >= 0)
            this.table = Dispatch.call(this.tables, "Item", new Variant(tableIndex)).toDispatch();
        return this.table;
    }
    /**
     * 在指定的单元格里填写数据
     *
     * @param tableIndex
     *            表格索引
     * @param cellRowIdx
     *            行索引
     * @param cellColIdx
     *            列索引
     * @param txt
     *            文本
     */
    public void putTxtToCell(int tableIndex, int cellRowIdx, int cellColIdx, String txt) {
        getTable(tableIndex);
        getCell(cellRowIdx, cellColIdx);
        Dispatch.call(this.cell, "Select");
        Dispatch.put(this.selection, "Text", txt);
    }
    /**
     * 在当前文档末尾拷贝来自另一个文档中的段落
     *
     * @param anotherDocPath
     *            另一个文档的磁盘路径
     * @param paragraphIndex
     *            被拷贝的段落在另一格文档中的序号(从1开始)
     */
    public void copyParagraphFromAnotherDoc(String anotherDocPath, int paragraphIndex) {
        Dispatch wordContent = Dispatch.get(this.document, "Content").toDispatch(); // 取得当前文档的内容
        Dispatch.call(wordContent, "InsertAfter", "$selection$");// 插入特殊符定位插入点
        copyParagraphFromAnotherDoc(anotherDocPath, paragraphIndex, "$selection$");
    }
    /**
     * 在当前文档指定的位置拷贝来自另一个文档中的段落
     *
     * @param anotherDocPath
     *            另一个文档的磁盘路径
     * @param paragraphIndex
     *            被拷贝的段落在另一格文档中的序号(从1开始)
     * @param pos
     *            当前文档指定的位置
     */
    public void copyParagraphFromAnotherDoc(String anotherDocPath, int paragraphIndex, String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath).toDispatch();
            Dispatch paragraphs = Dispatch.get(doc2, "Paragraphs").toDispatch();
            Dispatch paragraph = Dispatch.call(paragraphs, "Item", new Variant(paragraphIndex)).toDispatch();
            Dispatch range = Dispatch.get(paragraph, "Range").toDispatch();
            Dispatch.call(range, "Copy");
            if (this.find(pos)) {
                getRange();
                Dispatch.call(this.range, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(true));
                doc2 = null;
            }
        }
    }
    /**
     * 在当前文档指定的位置拷贝来自另一个文档中的表格
     *
     * @param anotherDocPath
     *            另一个文档的磁盘路径
     * @param tableIndex
     *            被拷贝的表格在另一格文档中的序号(从1开始)
     * @param pos
     *            当前文档指定的位置
     */
    public void copyTableFromAnotherDoc(String anotherDocPath, int tableIndex,
                                        String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath)
                    .toDispatch();
            Dispatch tables = Dispatch.get(doc2, "Tables").toDispatch();
            Dispatch table = Dispatch.call(tables, "Item",
                    new Variant(tableIndex)).toDispatch();
            Dispatch range = Dispatch.get(table, "Range").toDispatch();
            Dispatch.call(range, "Copy");
            if (this.find(pos)) {
                getRange();
                Dispatch.call(this.range, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(true));
                doc2 = null;
            }
        }
    }
    /**
     * 在当前文档指定的位置拷贝来自另一个文档中的图片
     *
     * @param anotherDocPath
     *            另一个文档的磁盘路径
     * @param shapeIndex
     *            被拷贝的图片在另一格文档中的位置
     * @param pos
     *            当前文档指定的位置
     */
    public void copyImageFromAnotherDoc(String anotherDocPath, int shapeIndex,
                                        String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath)
                    .toDispatch();
            Dispatch shapes = Dispatch.get(doc2, "InLineShapes").toDispatch();
            Dispatch shape = Dispatch.call(shapes, "Item",
                    new Variant(shapeIndex)).toDispatch();
            Dispatch imageRange = Dispatch.get(shape, "Range").toDispatch();
            Dispatch.call(imageRange, "Copy");
            if (this.find(pos)) {
                getRange();
                Dispatch.call(this.range, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(true));
                doc2 = null;
            }
        }
    }
    /**
     * 在指定的表格的指定行前面增加行
     *
     * @param tableIndex
     *            word文件中的第N张表(从1开始)
     * @param rowIndex
     *            指定行的序号(从1开始)
     */
    public void addTableRow(int tableIndex, int rowIndex) {
        getTable(tableIndex);
        getTableRows();
        getTableRow(rowIndex);
        Dispatch.call(this.rows, "Add", new Variant(this.row));
    }
    /**
     * 在第1行前增加一行
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addFirstTableRow(int tableIndex) {
        getTable(tableIndex);
        getTableRows();
        Dispatch row = Dispatch.get(rows, "First").toDispatch();
        Dispatch.call(this.rows, "Add", new Variant(row));
    }
    /**
     * 在最后1行前增加一行
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addLastTableRow(int tableIndex) {
        getTable(tableIndex);
        getTableRows();
        Dispatch row = Dispatch.get(this.rows, "Last").toDispatch();
        Dispatch.call(this.rows, "Add", new Variant(row));
    }
    /**
     * 增加一行
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addRow(int tableIndex) {
        getTable(tableIndex);
        getTableRows();
        Dispatch.call(this.rows, "Add");
    }
    /**
     * 增加一列
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addCol(int tableIndex) {
        getTable(tableIndex);
        getTableColumns();
        Dispatch.call(this.cols, "Add").toDispatch();
        Dispatch.call(this.cols, "AutoFit");
    }
    /**
     * 在指定列前面增加表格的列
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     * @param colIndex
     *            指定列的序号 (从1开始)
     */
    public void addTableCol(int tableIndex, int colIndex) {
        getTable(tableIndex);
        getTableColumns();
        getTableColumn(colIndex);
        Dispatch.call(this.cols, "Add", this.col).toDispatch();
        Dispatch.call(this.cols, "AutoFit");
    }
    /**
     * 在第1列前增加一列
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addFirstTableCol(int tableIndex) {
        getTable(tableIndex);
        Dispatch cols = getTableColumns();
        Dispatch col = Dispatch.get(cols, "First").toDispatch();
        Dispatch.call(cols, "Add", col).toDispatch();
        Dispatch.call(cols, "AutoFit");
    }
    /**
     * 在最后一列前增加一列
     *
     * @param tableIndex
     *            word文档中的第N张表(从1开始)
     */
    public void addLastTableCol(int tableIndex) {
        getTable(tableIndex);
        Dispatch cols = getTableColumns();
        Dispatch col = Dispatch.get(cols, "Last").toDispatch();
        Dispatch.call(cols, "Add", col).toDispatch();
        Dispatch.call(cols, "AutoFit");
    }
    /**
     * 获取当前表格的列数
     *
     * @return 列总数
     */
    public int getTableColumnsCount() {
        if (this.table == null)
            return 0;
        return Dispatch.get(this.cols, "Count").getInt();
    }
    /**
     * 获取当前表格的行数
     *
     * @return 行总数
     */
    public int getTableRowsCount() {
        if (this.table == null)
            return 0;
        return Dispatch.get(this.rows, "Count").getInt();
    }
    /**
     * 获取当前表格的所有列对象
     *
     * @return cols
     */
    public Dispatch getTableColumns() {
        if (this.table == null)
            return this.cols;
        this.cols = Dispatch.get(this.table, "Columns").toDispatch();
        return this.cols;
    }
    /**
     * 获取当前表格的所有行对象
     *
     * @return rows
     */
    public Dispatch getTableRows() {
        if (this.table == null)
            return this.rows;
        this.rows = Dispatch.get(this.table, "Rows").toDispatch();
        return this.rows;
    }
    /**
     * 根据索引获得当前表格的列对象
     *
     * @param columnIndex
     *            列索引
     * @return col
     */
    public Dispatch getTableColumn(int columnIndex) {
        if (this.cols == null)
            this.getTableColumns();
        if (columnIndex >= 0)
            this.col = Dispatch.call(this.cols, "Item",
                    new Variant(columnIndex)).toDispatch();
        return this.col;
    }
    /**
     * 根据索引获得当前表格的行对象
     *
     * @param rowIndex
     *            行索引
     * @return row
     */
    public Dispatch getTableRow(int rowIndex) {
        if (this.rows == null)
            this.getTableRows();
        if (rowIndex >= 0)
            this.row = Dispatch.call(this.rows, "Item", new Variant(rowIndex))
                    .toDispatch();
        return this.row;
    }
    /**
     * 自动调整当前所有表格
     */
    public void autoFitTable() {
        int count = this.getTablesCount();
        for (int i = 0; i < count; i++) {
            Dispatch table = Dispatch.call(tables, "Item", new Variant(i + 1))
                    .toDispatch();
            Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
            Dispatch.call(cols, "AutoFit");
        }
    }
    /**
     * 根据行索引与列索引获取当前表格中的单元格
     *
     * @param cellRowIdx
     *            行索引
     * @param cellColIdx
     *            列索引
     * @return cell对象
     */
    public Dispatch getCell(int cellRowIdx, int cellColIdx) {
        if (this.table == null)
            return this.cell;
        if (cellRowIdx >= 0 && cellColIdx >= 0)
            this.cell = Dispatch.call(this.table, "Cell",
                    new Variant(cellRowIdx), new Variant(cellColIdx))
                    .toDispatch();
        return this.cell;
    }
    public void selectCell(int cellRowIdx, int cellColIdx) {
        if (this.table == null)
            return;
        getCell(cellRowIdx, cellColIdx);
        if (cellRowIdx >= 0 && cellColIdx >= 0)
            Dispatch.call(this.cell, "select");
    }
    /**
     * 设置当前文档的标题
     *
     * @param title 标题
     * @param alignmentType 对齐方式
     */
    public void setTitle(String title, int alignmentType) {
        if (title == null || "".equals(title))
            return;
        if (this.alignment == null)
            this.getAlignment();
        if(alignmentType != 0 && alignmentType != 1 && alignmentType != 2)
            alignmentType = 0;
        Dispatch.put(this.alignment, "Alignment", alignmentType);
        Dispatch.call(this.selection, "TypeText", title);
    }
    /**
     * 设置当前表格边框的粗细
     *
     * @param width
     *            范围：1 < w < 13， 如果是0，就代表?]有框<br/>
     */
    public void setTableBorderWidth(int width) {
        if (this.table == null){
            return;
        }

        /*
         * 设置表格线的粗细
         * 1：代表最上边一条线
         * 2：代表最左边一条线
         * 3：最下边一条线
         * 4：最右边一条线
         * 5：除最上边最下边之外的所有横线
         * 6：除最左边最右边之外的所有竖线
         * 7：从左上角到右下角的斜线
         * 8：从左下角到右上角的斜线
         *
         */
        Dispatch borders = Dispatch.get(table, "Borders").toDispatch();
        Dispatch border = null;
        for (int i = 1; i < 7; i++) {
            border = Dispatch.call(borders, "Item", new Variant(i))
                    .toDispatch();
            if (width != 0) {
                Dispatch.put(border, "LineWidth", new Variant(width));
                Dispatch.put(border, "Visible", new Variant(true));
            } else if (width == 0) {
                Dispatch.put(border, "Visible", new Variant(false));
            }
        }
    }
    /**
     * 得到指定的表格指定的单元格中的值
     *
     * @param tableIndex
     *            表格索引（从1开始）
     * @param rowIndex
     *            行索引（从1开始）
     * @param colIndex
     *            列索引（从1开始）
     * @return
     */
    public String getTxtFromCell(int tableIndex, int rowIndex, int colIndex) {
        String value = "";
        // 设置为当前表格
        getTable(tableIndex);
        getCell(rowIndex, colIndex);
        if (cell != null) {
            Dispatch.call(cell, "Select");
            value = Dispatch.get(selection, "Text").toString();
            value = value.substring(0, value.length() - 2); // 去掉最后的回车符;
        }
        return value;
    }
    /**
     * 对当前选中的内容设置项目符号与列表
     * @param tabIndex
     *            <ul>
     *            <li>1.项目编号</li>
     *            <li>2.编号</li>
     *            <li>3.多级编号</li>
     *            <li>4.列表样式</li>
     *            </ul>
     * @param index
     *            0表示没有，其它数字代表是该tab页中的第几项内容
     */
    public void applyListTemplate(int tabIndex, int index) {
        // 取得ListGalleries对象列表
        Dispatch listGalleries = Dispatch.get(this.word, "ListGalleries")
                .toDispatch();
        // 取得列表中一个对象
        Dispatch listGallery = Dispatch.call(listGalleries, "Item",
                new Variant(tabIndex)).toDispatch();
        Dispatch listTemplates = Dispatch.get(listGallery, "ListTemplates")
                .toDispatch();
        if (this.range == null)
            this.getRange();
        Dispatch listFormat = Dispatch.get(this.range, "ListFormat")
                .toDispatch();
        Dispatch.call(listFormat, "ApplyListTemplate",
                Dispatch.call(listTemplates, "Item", new Variant(index)),
                new Variant(true), new Variant(1), new Variant(0));
    }

    /**
     * 增加文档目录
     */
    public void addTablesOfContents() {
        // 取得ActiveDocument、TablesOfContents、range对象
        Dispatch ActiveDocument = word.getProperty("ActiveDocument")
                .toDispatch();
        Dispatch TablesOfContents = Dispatch.get(ActiveDocument,
                "TablesOfContents").toDispatch();
        Dispatch range = Dispatch.get(this.selection, "Range").toDispatch();
        // 增加目录
        Dispatch.call(TablesOfContents, "Add", range, new Variant(true),
                new Variant(1), new Variant(3), new Variant(true), new Variant(
                        ""), new Variant(true), new Variant(true));
    }

    /**
     * 设置当前selection对齐方式
     *
     * @param alignmentType
     *            <ul>
     *            <li>0.居左</li>
     *            <li>1.居中</li>
     *            <li>2.居右</li>
     *            </ul>
     */
    public void setAlignment(int alignmentType) {
        if (this.alignment == null)
            this.getAlignment();
        Dispatch.put(this.alignment, "Alignment", alignmentType);
    }

    /**
     * 获取当前selection的对齐方式
     *
     * @return alignment
     */
    public Dispatch getAlignment() {
        if (this.selection == null)
            this.getSelection();
        this.alignment = Dispatch.get(this.selection, "ParagraphFormat")
                .toDispatch();
        return this.alignment;
    }

    /**
     * 获取字体对象
     *
     * @return font
     */
    public Dispatch getFont() {
        if (this.selection == null)
            this.getSelection();
        this.font = Dispatch.get(this.selection, "Font").toDispatch();
        return this.font;
    }

    /**
     * 设置当前selection的字体
     *
     * @param fontName
     *            字体名称，如“微软雅黑”
     * @param isBold
     *            是否粗体
     * @param isItalic
     *            是否斜体
     * @param isUnderline
     *            是否下划线
     * @param rgbColor
     *            颜色值"1,1,1,1"
     * @param Scale
     *            字体间距
     * @param fontSize
     *            字体大小
     */
    @Deprecated
    public void setFontScale(String fontName, boolean isBold, boolean isItalic,
                             boolean isUnderline, String rgbColor, int Scale, int fontSize) {
        Dispatch.put(this.font, "Name", fontName);
        Dispatch.put(this.font, "Bold", isBold);
        Dispatch.put(this.font, "Italic", isItalic);
        Dispatch.put(this.font, "Underline", isUnderline);
        Dispatch.put(this.font, "Color", rgbColor);
        Dispatch.put(this.font, "Scaling", Scale);
        Dispatch.put(this.font, "Size", fontSize);
    }


    /**
     * 设置当前选定内容的字体
     * @param isBold 是否为粗体
     * @param isItalic 是否为斜体
     * @param isUnderLine 是否带下划线
     * @param color rgb 字体颜色 例如：红色 255,0,0
     * @param size 字体大小  12:小四  16:三号
     * @param name 字体名称  例如：宋体，新宋体，楷体，隶书
     */
    public void setFont(boolean isBold,boolean isItalic,boolean isUnderLine,String color,String size,String name) {
        Dispatch font = Dispatch.get(getSelection(), "Font").toDispatch();
        Dispatch.put(font, "Name", new Variant(name));
        Dispatch.put(font, "Bold", new Variant(isBold));
        Dispatch.put(font, "Italic", new Variant(isItalic));
        Dispatch.put(font, "Underline", new Variant(isUnderLine));
        if(!"".equals(color))
            Dispatch.put(font, "Color", color);
        Dispatch.put(font, "Size", size);
    }

    /**
     * 保存文件
     * @param outputPath
     * 保存路径
     */
    public void saveAs(String outputPath) {
        if (this.document == null)
            return;
        if (outputPath == null || "".equals(outputPath))
            return;
        Dispatch.call(this.document, "SaveAs", outputPath);
    }

    /**
     * 另存为HTML内容
     * @param htmlFile
     *            html文件路径
     */
    public void saveAsHtml(String htmlFile) {
        Dispatch.invoke(this.document, "SaveAs", Dispatch.Method, new Object[] {
                htmlFile, new Variant(8) }, new int[1]);
    }

    /**
     * 关闭当前word文档
     */
    public void close() {
        if (document == null)
            return;
        Dispatch.call(document, "Close", new Variant(0));
    }

    /**
     * 执行当前文档打印命令
     */
    public void printFile() {
        if (document == null)
            return;
        Dispatch.call(document, "PrintOut");
    }
    /**
     * 退出Microsoft Office Word程序
     */
    public void quit() {
        word.invoke("Quit", new Variant[0]);
        ComThread.Release();
    }

    /**
     * 选中整篇文档
     */
    public void selectAllContent(){
        Dispatch.call(this.document,"select");
    }

    /**
     * 复制整篇文档
     */
    public void copy(){
        Dispatch.call(this.document,"select");
        Dispatch.call(this.selection,"copy");
    }

    /**
     * 在当前插入点位置粘贴选中的内容
     */
    public void paste(){
        Dispatch.call(this.selection,"paste");
    }

    /**
     * html文件转为Word
     * @param html
     * @param wordFile
     */
    public void html2Word(String html, String wordFile){
        openDocument(wordFile);
        // 清空文档内容
        selectAllContent();
        insertText(null);
        Dispatch.invoke(this.selection, "InsertFile", Dispatch.Method, new Object[] { html, "", new Variant(false), new Variant(false), new Variant(false) }, new int[3]);
    }

}