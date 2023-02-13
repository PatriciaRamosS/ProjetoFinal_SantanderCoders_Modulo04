package impl;

import dominio.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CampeonatoBrasileiroImpl {

    private Map<Integer, List<Jogo>> brasileirao;
    private final List<Jogo> jogos;
    private Predicate<Jogo> filtro;

    public CampeonatoBrasileiroImpl(Path arquivo, Predicate<Jogo> filtro) throws IOException {
        this.jogos = lerArquivo(arquivo);
        this.filtro = filtro;
    }

    public List<Jogo> lerArquivo(Path filePath) throws IOException {
        List<Jogo> jogos = new ArrayList<>();
        Scanner scanner = new Scanner(filePath);
        if (scanner.hasNextLine()) {
            // pula a primeira linha
            scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //System.out.println(line);
            String[] splittedLine = line.split(";");

            Integer rodada = Integer.valueOf(splittedLine[0]);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(splittedLine[1], dateFormatter);

            if (localDate.getYear() == 2019) {
            //if (localDate.getYear() == 2020 || localDate.getYear() == 2021) {

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime localTime = LocalTime.parse(splittedLine[2], timeFormatter);
                DayOfWeek dayOfWeek = getDayOfWeek(splittedLine[3]);
                DataDoJogo dataDoJogo = new DataDoJogo(localDate, localTime, dayOfWeek);

                Time mandante = new Time(splittedLine[4]);
                Time visitante = new Time(splittedLine[5]);
                Time vencedor = new Time(splittedLine[6]);

                String arena = splittedLine[7];

                Integer mandantePlacar = Integer.valueOf(splittedLine[8]);
                Integer visitantePlacar = Integer.valueOf(splittedLine[9]);

                String estadoMandante = splittedLine[10];
                String estadoVisitante = splittedLine[11];
                String estadoVencedor = splittedLine[12];

                Jogo jogo = new Jogo(rodada, dataDoJogo, mandante, visitante, vencedor, arena, mandantePlacar, visitantePlacar, estadoMandante, estadoVisitante, estadoVencedor);
                jogos.add(jogo);

            }

        }
        return jogos;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        return jogos.stream().mapToInt(jogo -> jogo.mandantePlacar() + jogo.visitantePlacar()).summaryStatistics();
    }

    public Map<Jogo, Integer> getMediaGolsPorJogo() {
        return null;
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        return null;
    }

    public List<Jogo> todosOsJogos() {
        return null;
    }

    public Long getTotalVitoriasEmCasa() {
        int totalDeVitoriasEmCasa = 0;
        for (Jogo jogo : jogos) {
            if (jogo.mandantePlacar() > jogo.visitantePlacar()) {
                totalDeVitoriasEmCasa++;
            }
        }
        return (long) totalDeVitoriasEmCasa;
    }

    public Long getTotalVitoriasForaDeCasa() {
        int totalDeVitoriasForaDeCasa = 0;
        for (Jogo jogo : jogos) {
            if (jogo.mandantePlacar() < jogo.visitantePlacar()) {
                totalDeVitoriasForaDeCasa++;
            }
        }
        return (long) totalDeVitoriasForaDeCasa;
    }

    public Long getTotalEmpates() {
        int totalDeEmpates = 0;
        for (Jogo jogo : jogos) {
            if (Objects.equals(jogo.mandantePlacar(), jogo.visitantePlacar())) {
                totalDeEmpates++;
            }
        }
        return (long) totalDeEmpates;
    }

    public Long getTotalJogosComMenosDe3Gols() {
        int totalDeJogosComMenosDe3Gols = 0;
        for (Jogo jogo : jogos) {
            if (jogo.mandantePlacar() + jogo.visitantePlacar() < 3) {
                totalDeJogosComMenosDe3Gols++;
            }
        }
        return (long) totalDeJogosComMenosDe3Gols;
    }

    public Long getTotalJogosCom3OuMaisGols() {
        int totalDeJogosComMaisDe3Gols = 0;
        for (Jogo jogo : jogos) {
            if (jogo.mandantePlacar() + jogo.visitantePlacar() > 2) {
                totalDeJogosComMaisDe3Gols++;
            }
        }
        return (long) totalDeJogosComMaisDe3Gols;
    }

    public Map<Resultado, Long> getTodosOsPlacares() {
        return null;
    }

    public Map.Entry<Resultado, Long> getPlacarMaisRepetido() {
        Map<Resultado, Long> ocorrencias = new HashMap<>();
        for (Jogo jogo : jogos) {
            Resultado resultado = new Resultado(jogo.mandantePlacar(), jogo.visitantePlacar());
            Long count = ocorrencias.getOrDefault(resultado, 0L);
            ocorrencias.put(resultado, count + 1);
        }
        return Collections.max(ocorrencias.entrySet(), Map.Entry.comparingByValue());
    }

    public Map.Entry<Resultado, Long> getPlacarMenosRepetido() {
        Map<Resultado, Long> ocorrencias = new HashMap<>();
        for (Jogo jogo : jogos) {
            Resultado resultado = new Resultado(jogo.mandantePlacar(), jogo.visitantePlacar());
            Long count = ocorrencias.getOrDefault(resultado, 0L);
            ocorrencias.put(resultado, count + 1);
        }
        return Collections.min(ocorrencias.entrySet(), Map.Entry.comparingByValue());
    }

    private List<Time> getTodosOsTimes() {
        Set<Time> times = new HashSet<>();
        for (Jogo jogo : jogos) {
            Time time = new Time(jogo.mandante().nome());
            times.add(time);
        }
        return times.stream().toList();
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {
        return null;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        return null;
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        return null;
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return null;
    }

    public List<PosicaoTabela> getTabela() {
        List<Time> times = getTodosOsTimes();
        List<PosicaoTabela> tabela = new ArrayList<>();

        for (Time time : times) {
            Long vitorias = jogos.stream().filter(jogo -> jogo.vencedor().equals(time)).count();
            Long empates = jogos.stream().filter(jogo -> (jogo.mandante().equals(time) || jogo.visitante().equals(time)) && jogo.vencedor().nome().equals("-")).count();
            Long quantidadeDeJogos = jogos.stream().filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time)).count();
            Long derrotas = quantidadeDeJogos - vitorias - empates;
            Long golsPositivos = 0L;
            Long golsSofridos = 0L;
            long saldoDeGols;
            for (Jogo jogo : jogos) {
                if (jogo.mandante().equals(time)) {
                    golsPositivos += jogo.mandantePlacar();
                    golsSofridos += jogo.visitantePlacar();
                }
                if (jogo.visitante().equals(time)) {
                    golsPositivos += jogo.visitantePlacar();
                    golsSofridos += jogo.mandantePlacar();
                }
            }
            saldoDeGols = golsPositivos - golsSofridos;

            PosicaoTabela posicaoTabela = new PosicaoTabela(
                time,
                vitorias,
                derrotas,
                empates,
                golsPositivos,
                golsSofridos,
                saldoDeGols,
                quantidadeDeJogos
            );
            tabela.add(posicaoTabela);
        }

        List<PosicaoTabela> tabelaOrdenada = tabela.stream().sorted(
                Comparator.comparingInt(PosicaoTabela::getPontos)
                .thenComparing(PosicaoTabela::saldoDeGols)
                .reversed()
        ).toList();

        return tabelaOrdenada;
    }

    private DayOfWeek getDayOfWeek(String dia) {
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("pt", "BR"));
        return DayOfWeek.from(dayOfWeekFormatter.parse(dia.toLowerCase()));
    }

    private Map<Integer, Integer> getTotalGolsPorRodada() {
        return null;
    }

    private Map<Time, Integer> getTotalDeGolsPorTime() {
        return null;
    }

    private Map<Integer, Double> getMediaDeGolsPorRodada() {
        return null;
    }


}