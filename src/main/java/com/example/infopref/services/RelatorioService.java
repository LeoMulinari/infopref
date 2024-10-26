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
import com.itextpdf.text.pdf.PdfWriter;

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
            PdfWriter.getInstance(document, out);
            document.open();

            // Carregar a logo da empresa
            try {
                Image logo = Image.getInstance("infopref-back\\src\\main\\resources\\logo.png"); // Caminho para a logo
                logo.scaleToFit(50, 50); // Ajuste de tamanho
                logo.setAlignment(Image.ALIGN_LEFT); // Alinhamento para o canto superior esquerdo
                document.add(logo);
            } catch (IOException e) {
                e.printStackTrace(); // Lida com exceções relacionadas ao carregamento da imagem
            }

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            // Título do relatório
            Paragraph title = new Paragraph("Relatório de Ordens de Serviço", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Subtítulo com período e tipo de filtro
            String periodo = "Período:  " + formatDate(dataInicio) + "   a   " + formatDate(dataFim);
            String filtroTexto = getFiltroDescricao(tipo, filtro);

            // Adicionando período e filtro em linhas separadas
            Paragraph periodoParagraph = new Paragraph(periodo, subTitleFont);
            periodoParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(periodoParagraph);

            Paragraph filtroParagraph = new Paragraph(filtroTexto, subTitleFont);
            filtroParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            filtroParagraph.setSpacingBefore(10f); // Adiciona espaçamento entre tipo e filtro
            document.add(filtroParagraph);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            // Formatter para a data no formato dd-MM-yyyy
            DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Ajuste na criação do card no método `gerarRelatorio`
            for (OrdemServico os : ordens) {
                // Criando uma tabela principal de 1 coluna para representar o card completo
                PdfPTable cardTable = new PdfPTable(1);
                cardTable.setWidthPercentage(100);
                cardTable.setSpacingBefore(10f);

                // Criação de um "card" para cada ordem de serviço
                PdfPCell cardCell = new PdfPCell();
                cardCell.setPadding(10);

                // Número de Protocolo (Centralizado e com linha separadora abaixo)
                Paragraph protocolo = new Paragraph("Número de Protocolo: " + os.getId(),
                        new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                protocolo.setAlignment(Paragraph.ALIGN_CENTER);
                cardCell.addElement(protocolo);

                // Criando uma tabela interna de 2 colunas para os detalhes
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(2f);

                // Primeira coluna de informações
                PdfPCell leftCell = new PdfPCell();
                leftCell.setBorder(PdfPCell.NO_BORDER);

                String dataAberturaFormatada = os.getData_abertura().format(localDateFormatter);
                leftCell.addElement(new Paragraph("Data Abertura: " + dataAberturaFormatada));

                if (os.getEquipamentos() != null && !os.getEquipamentos().isEmpty()) {
                    StringBuilder equipamentosStr = new StringBuilder("Nº de Patrimônio: ");
                    for (Equipamento equipamento : os.getEquipamentos()) {
                        equipamentosStr.append(equipamento.getNum_patrimonio()).append(", ");
                    }
                    equipamentosStr.setLength(equipamentosStr.length() - 2); // Remove a última vírgula e espaço
                    leftCell.addElement(new Paragraph(equipamentosStr.toString()));
                } else {
                    leftCell.addElement(new Paragraph("Equipamentos: Nenhum"));
                }

                leftCell.addElement(new Paragraph("Tipo Chamado: " + os.getTipo_chamado().getDescricao()));
                leftCell.addElement(new Paragraph("Status: " + os.getStatus().getDisplayName()));

                // Segunda coluna de informações
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

                // Adiciona as duas células de informações à tabela interna
                infoTable.addCell(leftCell);
                infoTable.addCell(rightCell);

                // Adiciona a tabela de informações ao card principal
                cardCell.addElement(infoTable);
                cardTable.addCell(cardCell);

                // Adiciona o card completo ao documento
                document.add(cardTable);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Método para formatar a data para exibição
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

    // Método para descrever o filtro utilizado no relatório
    private String getFiltroDescricao(String tipo, String filtro) {
        switch (tipo.toLowerCase()) {
            case "tipo_chamado":
                // Aqui, obter a descrição do enum com base no valor do filtro
                TipoChamado tipoChamado;
                try {
                    tipoChamado = TipoChamado.valueOf(filtro.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return "Tipo de Chamado: Desconhecido";
                }
                return "Relatório por Tipo de Chamado           Tipo: " + tipoChamado.getDescricao();
            case "solicitante":
                return "Relatório por Solicitante               Solicitante: "
                        + getSolicitanteNome(Long.parseLong(filtro));
            case "departamento":
                return "Relatório por Departamento              " + getDepartamentoNome(Long.parseLong(filtro));
            case "secretaria":
                return "Relatório por Secretaria                " + getSecretariaNome(Long.parseLong(filtro));
            case "tecnico":
                return "Relatório por Técnico                   Técnico: " + getTecnicoNome(Long.parseLong(filtro));
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

        // Definir todos os filtros como `null` inicialmente
        TipoChamado tipoChamado = null;
        Long solicitanteId = null;
        Long departamentoId = null;
        Long secretariaId = null;
        Long tecnicoId = null;

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

        // Chamar o método de filtro do repositório
        return ordemServicoRepository.findByDateRangeAndFilters(
                inicio, fim, tipoChamado, solicitanteId, departamentoId, secretariaId, tecnicoId);
    }

}
