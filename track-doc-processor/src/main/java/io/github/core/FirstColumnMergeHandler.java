package io.github.core;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.util.List;

public class FirstColumnMergeHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder,
                                 WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList,
                                 Cell cell,
                                 Head head,
                                 Integer relativeRowIndex,
                                 Boolean isHead) {

        if (isHead) {
            return;
        }
        int columnIndex = cell.getColumnIndex();
        int rowIndex = cell.getRowIndex();
        // 只处理前3列
        if (columnIndex > 3) {
            return;
        }
        Sheet sheet = writeSheetHolder.getSheet();
        Row curRow = cell.getRow();
        Row preRow = sheet.getRow(rowIndex - 1);
        if (curRow == null || preRow == null) {
            return;
        }
        Cell curCell = curRow.getCell(columnIndex);
        Cell preCell = preRow.getCell(columnIndex);
        if (curCell == null || preCell == null) {
            return;
        }
        String curData = curCell.getStringCellValue();
        String preData = preCell.getStringCellValue();
        if (curData.equals(preData)) {
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergedRegions.size(); i++) {
                CellRangeAddress region = mergedRegions.get(i);
                if (region.isInRange(rowIndex - 1, columnIndex)) {
                    sheet.removeMergedRegion(i);
                    region.setLastRow(rowIndex);
                    sheet.addMergedRegion(region);
                    setRegionBorder(region, sheet);
                    isMerged = true;
                    break;
                }
            }
            if (!isMerged) {
                CellRangeAddress region = new CellRangeAddress(rowIndex - 1, rowIndex, columnIndex, columnIndex);
                sheet.addMergedRegion(region);

                setRegionBorder(region, sheet);
            }
        }
    }

    /**
     * 单元格加边框
     */
    private void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }
}
