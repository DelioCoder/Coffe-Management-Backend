package com.DelioCoder.cafe.utils.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;

public class MyTable {

    PDDocument document;

    PDPageContentStream contentStream;

    private int[] colWidths;
    private int cellHeight;

    private int yPosition;

    private int xPosition;

    private int colPosition;

    private int xInitialPosition;

    private float fontSize;

    private PDFont font;

    private Color fontColor;

    public MyTable(PDDocument document, PDPageContentStream contentStream) {
        this.document = document;
        this.contentStream = contentStream;
    }

    public void setTable(int[] colWidths, int cellHeight, int xPosition, int yPosition)
    {
        this.colWidths = colWidths;
        this.cellHeight = cellHeight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        xInitialPosition = xPosition;
    }

    public void setTableFont(PDFont font, float fontSize, Color fontColor)
    {

        this.font = font;
        this.fontSize = fontSize;
        this.fontColor = fontColor;

    }

    public void addCell(String text, Color fillColor) throws IOException
    {
        contentStream.setStrokingColor(1f);

        if(fillColor != null)
        {
            contentStream.setNonStrokingColor(fillColor);
        }

        contentStream.addRect(xPosition, yPosition, colWidths[colPosition], cellHeight);

        if(fillColor == null)
        {
            contentStream.stroke();
        }else {
            contentStream.fillAndStroke();
        }

        contentStream.beginText();
        contentStream.setNonStrokingColor(fontColor);

        if(colPosition == 4 || colPosition == 2)
        {
            float fontWidth = font.getStringWidth(text)/1000 * fontSize;
            contentStream.newLineAtOffset(xPosition + colWidths[colPosition] - 20 - fontWidth, yPosition + 10);
        }else {
            contentStream.newLineAtOffset(xPosition + 20, yPosition + 10);
        }

        contentStream.showText(text);
        contentStream.endText();

        xPosition = xPosition + colWidths[colPosition];
        colPosition++;

        if(colPosition == colWidths.length)
        {
            colPosition = 0;
            xPosition = xInitialPosition;
            yPosition-=cellHeight;
        }
    }

}
