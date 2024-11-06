package com.example.infopref.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

class PageNumberEvent extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        String text = "" + writer.getPageNumber();
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Paragraph(text),
                (document.right() + document.left()) / 2, document.bottom() - 10, 0);
    }
}
