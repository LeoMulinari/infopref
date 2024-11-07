package com.example.infopref.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Equipamento;
import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.Enums.TipoChamado;
import com.example.infopref.repositories.DepartamentoRepository;
import com.example.infopref.repositories.OrdemServicoRepository;
import com.example.infopref.repositories.SecretariaRepository;
import com.example.infopref.repositories.SolicitanteRepository;
import com.example.infopref.repositories.TecnicoRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;;

@Service
public class RelatorioService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;
    @Autowired
    private SolicitanteRepository solicitanteRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private SecretariaRepository secretariaRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public ByteArrayInputStream gerarRelatorio(String dataInicio, String dataFim, String tipo, String filtro) {
        List<OrdemServico> ordens = filtrarOrdens(dataInicio, dataFim, tipo, filtro);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);

            writer.setPageEvent(new PageNumberEvent());

            document.open();

            try {
                Image logo = Image.getInstance("src\\main\\java\\com\\example\\infopref\\services\\logo.png");
                logo.scaleToFit(60, 60);
                logo.setAlignment(Image.ALIGN_LEFT);
                document.add(logo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            String tituloRelatorio = "Relatório de Ordens de Serviço por " + getFiltroTitulo(tipo);
            Paragraph title = new Paragraph(tituloRelatorio, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            String dataGeracao = "Data de Geração: "
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph dataParagraph = new Paragraph(dataGeracao, subTitleFont);
            dataParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(dataParagraph);

            String periodo = "Período: " + formatDate(dataInicio) + " a " + formatDate(dataFim);
            Paragraph periodoParagraph = new Paragraph(periodo, subTitleFont);
            periodoParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(periodoParagraph);

            String filtroTexto = getFiltroDescricao(tipo, filtro);

            Paragraph filtroParagraph = new Paragraph(filtroTexto, subTitleFont);
            filtroParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(filtroParagraph);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            for (OrdemServico os : ordens) {

                PdfPTable cardTable = new PdfPTable(1);
                cardTable.setWidthPercentage(100);
                cardTable.setSpacingBefore(10f);
                PdfPCell cardCell = new PdfPCell();
                cardCell.setPadding(10);

                Paragraph protocolo = new Paragraph("Número de Protocolo: " + os.getId(),
                        new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
                protocolo.setAlignment(Paragraph.ALIGN_LEFT);
                cardCell.addElement(protocolo);

                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(2f);

                PdfPCell leftCell = new PdfPCell();
                leftCell.setBorder(PdfPCell.NO_BORDER);
                DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                leftCell.addElement(
                        new Paragraph("Data Abertura: " + os.getData_abertura().format(localDateFormatter)));

                if (os.getEquipamentos() != null && !os.getEquipamentos().isEmpty()) {
                    StringBuilder equipamentosStr = new StringBuilder("Nº de Patrimônio: ");
                    for (Equipamento equipamento : os.getEquipamentos()) {
                        equipamentosStr.append(equipamento.getNum_patrimonio()).append(", ");
                    }
                    equipamentosStr.setLength(equipamentosStr.length() - 2);
                    leftCell.addElement(new Paragraph(equipamentosStr.toString()));
                } else {
                    leftCell.addElement(new Paragraph("Equipamentos: Nenhum"));
                }

                leftCell.addElement(new Paragraph("Tipo Chamado: " + os.getTipo_chamado().getDescricao()));
                leftCell.addElement(new Paragraph("Status: " + os.getStatus().getDisplayName()));

                PdfPCell rightCell = new PdfPCell();
                rightCell.setBorder(PdfPCell.NO_BORDER);
                rightCell.addElement(new Paragraph("Solicitante: " + os.getSolicitante().getNome()));
                rightCell.addElement(new Paragraph(
                        "Secretaria: " + os.getSolicitante().getDepartamento().getSecretaria().getNome()));
                rightCell.addElement(new Paragraph("Departamento: " + os.getSolicitante().getDepartamento().getNome()));

                if (os.getTecnico() != null) {
                    rightCell.addElement(new Paragraph("Técnico: " + os.getTecnico().getNome()));
                } else {
                    rightCell.addElement(new Paragraph("Técnico: Não atribuído"));
                }

                infoTable.addCell(leftCell);
                infoTable.addCell(rightCell);
                cardCell.addElement(infoTable);
                cardTable.addCell(cardCell);
                document.add(cardTable);
            }

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    private String getFiltroTitulo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "tipo_chamado":
                return "Tipo de Chamado";
            case "solicitante":
                return "Solicitante";
            case "departamento":
                return "Departamento";
            case "secretaria":
                return "Secretaria";
            case "tecnico":
                return "Técnico";
            default:
                return "";
        }
    }

    private String getFiltroDescricao(String tipo, String filtro) {
        switch (tipo.toLowerCase()) {
            case "tipo_chamado":
                TipoChamado tipoChamado;
                try {
                    tipoChamado = TipoChamado.valueOf(filtro.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return "Tipo de Chamado: Desconhecido";
                }
                return "Tipo: " + tipoChamado.getDescricao();
            case "solicitante":
                return "Solicitante: "
                        + getSolicitanteNome(Long.parseLong(filtro));
            case "departamento":
                return getDepartamentoNome(Long.parseLong(filtro));
            case "secretaria":
                return getSecretariaNome(Long.parseLong(filtro));
            case "tecnico":
                return "Técnico: " + getTecnicoNome(Long.parseLong(filtro));
            default:
                return "";
        }
    }

    private String getSolicitanteNome(Long id) {
        return solicitanteRepository.findById(id)
                .map(solicitante -> solicitante.getNome())
                .orElse("Desconhecido");
    }

    private String getDepartamentoNome(Long id) {
        return departamentoRepository.findById(id)
                .map(departamento -> departamento.getNome())
                .orElse("Desconhecido");
    }

    private String getSecretariaNome(Long id) {
        return secretariaRepository.findById(id)
                .map(secretaria -> secretaria.getNome())
                .orElse("Desconhecido");
    }

    private String getTecnicoNome(Long id) {
        return tecnicoRepository.findById(id)
                .map(tecnico -> tecnico.getNome())
                .orElse("Desconhecido");
    }

    private List<OrdemServico> filtrarOrdens(String dataInicio, String dataFim, String tipo, String filtro) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inicio, fim;
        try {
            inicio = LocalDate.parse(dataInicio, formatter);
            fim = LocalDate.parse(dataFim, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Erro ao converter datas para geração de relatório.");
        }

        TipoChamado tipoChamado = null;
        Long solicitanteId = null;
        Long departamentoId = null;
        Long secretariaId = null;
        Long tecnicoId = null;

        switch (tipo.toLowerCase()) {
            case "tipo_chamado":
                try {
                    tipoChamado = TipoChamado.valueOf(filtro.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Tipo de chamado inválido: " + filtro);
                }
                break;
            case "solicitante":
                solicitanteId = Long.parseLong(filtro);
                break;
            case "departamento":
                departamentoId = Long.parseLong(filtro);
                break;
            case "secretaria":
                secretariaId = Long.parseLong(filtro);
                break;
            case "tecnico":
                tecnicoId = Long.parseLong(filtro);
                break;
            default:
                throw new IllegalArgumentException("Tipo de filtro inválido: " + tipo);
        }

        return ordemServicoRepository.findByDateRangeAndFilters(
                inicio, fim, tipoChamado, solicitanteId, departamentoId, secretariaId, tecnicoId);
    }

}
