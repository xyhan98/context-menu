import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class GUI extends JFrame {
    public static final String[] COLUMNS = {"File path", "File name", "File extension", "File size", "MD5"};
    private String[][] data;
    private JTable table;
    private JScrollPane scrollPane;
    private int tableWidth;
    private static final int MIN_COLUMN_WIDTH = 120;
    private static final int MIN_ROW_HEIGHT = 30;
    private static final int TABLE_HEIGHT = 400;
    private static DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static {
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    public GUI(String[][] data) throws HeadlessException {
        super("File(s) Information");
        this.data = data;
        this.table = new JTable(this.data, COLUMNS);
        this.table.setBorder(new LineBorder(Color.BLACK));
        this.table.setRowHeight(MIN_ROW_HEIGHT);
        this.tableWidth = setOptimalColumnWidth(this.table);
        this.scrollPane = new JScrollPane(this.table);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setSize(this.tableWidth, TABLE_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private static int setOptimalColumnWidth(JTable table) {
        int tableWidth = 0;
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            int maxColumnWidth = MIN_COLUMN_WIDTH;
            TableColumn column = table.getColumnModel().getColumn(columnIndex);

            // Check header width
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComp = headerRenderer
                    .getTableCellRendererComponent(table, headerValue, false, false, 0, columnIndex);
            maxColumnWidth = Math.max(maxColumnWidth, headerComp.getPreferredSize().width);

            // Check cells width
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                Object cellValue = table.getValueAt(rowIndex, columnIndex);
                Component cellComp = cellRenderer
                        .getTableCellRendererComponent(table, cellValue, false, false, rowIndex, columnIndex);
                maxColumnWidth = Math.max(maxColumnWidth, cellComp.getPreferredSize().width);
            }

            // Set the width of the current column
            column.setPreferredWidth(maxColumnWidth);
            tableWidth += maxColumnWidth;

            // Center the content in the cells, except the first and last columns
            if (columnIndex != 0 && columnIndex != table.getColumnCount() - 1) {
                column.setCellRenderer(centerRenderer);
            }
        }
        return tableWidth;
    }
}
