package com.example.infopref.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.Enums.TipoChamado;
import com.example.infopref.repositories.OrdemServicoRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class RelatorioService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public ByteArrayInputStream gerarRelatorio(String dataInicio, String dataFim, String tipo, String filtro) {
        List<OrdemServico> ordens = filtrarOrdens(dataInicio, dataFim, tipo, filtro);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Paragraph para = new Paragraph("Relatório de Ordens de Serviço", font);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 3, 3, 3 });

            addTableHeader(table);
            addRows(table, ordens);

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell header = new PdfPCell();
        header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        header.setPadding(5);

        header.setPhrase(new Paragraph("ID"));
        table.addCell(header);

        header.setPhrase(new Paragraph("Solicitante"));
        table.addCell(header);

        header.setPhrase(new Paragraph("Data"));
        table.addCell(header);

        header.setPhrase(new Paragraph("Status"));
        table.addCell(header);
    }

    private void addRows(PdfPTable table, List<OrdemServico> ordens) {
        if (ordens == null || ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada.");
        }
        for (OrdemServico os : ordens) {
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(os.getId()))));
            table.addCell(new PdfPCell(new Paragraph(os.getSolicitante().getNome())));
            table.addCell(new PdfPCell(new Paragraph(os.getData_abertura().toString())));
            table.addCell(new PdfPCell(new Paragraph(os.getStatus().toString())));
        }
    }

    private List<OrdemServico> filtrarOrdens(String dataInicio, String dataFim, String tipo, String filtro) {
        System.out.println("Data Início: " + dataInicio);
        System.out.println("Data Fim: " + dataFim);
        System.out.println("Tipo: " + tipo);
        System.out.println("Filtro: " + filtro);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date inicio, fim;
        try {
            inicio = formatter.parse(dataInicio);
            fim = formatter.parse(dataFim);
        } catch (ParseException e) {
            throw new RuntimeException("Erro ao converter datas para geração de relatório.");
        }

        // Definir todos os filtros como `null` inicialmente
        TipoChamado tipoChamado = null;
        String solicitanteNome = null;
        String departamentoNome = null;
        String secretariaNome = null;
        String tecnicoNome = null;

        // Configurar o filtro específico com base no tipo
        switch (tipo.toLowerCase()) {
            case "tipo_chamado":
                try {
                    tipoChamado = TipoChamado.valueOf(filtro.toUpperCase()); // Converte string para enum
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Tipo de chamado inválido: " + filtro);
                }
                break;
            case "solicitante":
                solicitanteNome = filtro;
                break;
            case "departamento":
                departamentoNome = filtro;
                break;
            case "secretaria":
                secretariaNome = filtro;
                break;
            case "tecnico":
                tecnicoNome = filtro;
                break;
            default:
                throw new IllegalArgumentException("Tipo de filtro inválido: " + tipo);
        }
        System.out.println("Tipo Chamado: " + (tipoChamado != null ? tipoChamado : "Nenhum"));
        System.out.println("Solicitante Nome: " + (solicitanteNome != null ? solicitanteNome : "Nenhum"));
        System.out.println("Departamento Nome: " + (departamentoNome != null ? departamentoNome : "Nenhum"));
        System.out.println("Secretaria Nome: " + (secretariaNome != null ? secretariaNome : "Nenhum"));
        System.out.println("Técnico Nome: " + (tecnicoNome != null ? tecnicoNome : "Nenhum"));

        // Chamar o método de filtro do repositório
        return ordemServicoRepository.findByDateRangeAndFilters(
                inicio, fim, tipoChamado, solicitanteNome, departamentoNome, secretariaNome, tecnicoNome);
    }
}
