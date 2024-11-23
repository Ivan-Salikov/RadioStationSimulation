package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;


public class RadioStation {
    private final MusicLibrary musicLibrary; // Фонотека
    private final ObservableList<RadioProgram> radioPrograms; // Радиопрограммы
    private final RequestQueue requestQueue; // Очередь запросов
    private final ObservableList<Request> completedRequests; // Завершенные запросы

    private int totalSimulationDays; // Длительность симуляции (в днях)
    private int simulationStepSeconds; // Шаг симуляции (в секундах)
    private int currentDay; // Текущий день симуляции
    private int currentStep; // Текущий шаг симуляции

    private boolean isRunning; // Флаг работы симуляции

    private long lastRequestTime = 0;  // Для отслеживания времени между запросами
    private static final long REQUEST_DELAY = 500;  // Задержка между обработкой запросов в миллисекундах (0.5 секунды)

    // Статистика по жанрам
    private final Map<Genre, Integer> genrePopularity;

    public RadioStation(MusicLibrary musicLibrary, ObservableList<RadioProgram> radioPrograms, RequestQueue requestQueue) {
        this.musicLibrary = musicLibrary;
        this.radioPrograms = radioPrograms;
        this.requestQueue = requestQueue;
        this.completedRequests = FXCollections.observableArrayList();
        this.genrePopularity = new HashMap<>();
        this.isRunning = false;

        // Инициализация жанров
        for (Genre genre : Genre.values()) {
            genrePopularity.put(genre, 0);
        }
    }

    public void configureSimulation(int totalSimulationDays, int simulationStepSeconds) {
        this.totalSimulationDays = totalSimulationDays;
        this.simulationStepSeconds = simulationStepSeconds;
        this.currentDay = 0;
        this.currentStep = 0;

    }

    private void resetProgramsPlaylists(){
        for (RadioProgram program: radioPrograms) {
            program.clearPlaylist();
        }
    }


    public void startSimulation() {
        resetProgramsPlaylists();
        isRunning = true;
        currentDay = 1;
        currentStep = 0;
        System.out.println("Симуляция началась!");
    }

    public void stopSimulation() {
        isRunning = false;
        System.out.println("Симуляция остановлена.");
    }

    public boolean canContinueSimulation() {
        return isRunning && currentDay <= totalSimulationDays;
    }

    public void simulateStep() {
        if (!canContinueSimulation()) {
            stopSimulation();
            return;
        }

        generateRequests();

        processRequests();

        currentStep++;
        if (currentStep >= simulationStepSeconds) {
            currentStep = 0;
            currentDay++;
            System.out.println("Наступил новый день симуляции: " + currentDay);
        }
    }

    private void generateRequests(){
        int maxRequests = musicLibrary.size();
        Random random = new Random();

        int newRequestsCount = random.nextInt(Math.max(1, maxRequests)) + 1;
        for (int i = 0; i < newRequestsCount; i++) {
            String randomType = generateRandomRequestType();
            String randomValue = generateRandomRequestValue(randomType);

            Request newRequest = new Request(randomType, randomValue);
            requestQueue.addRequest(newRequest);
        }
        System.out.println("Сгенерировано запросов: " + newRequestsCount);
    }

    private String generateRandomRequestType() {
        String[] requestTypes = {"Track", "Author", "Album", "Artist"};
        return requestTypes[new Random().nextInt(requestTypes.length)];
    }

    private String generateRandomRequestValue(String requestType) {
        Random random = new Random();
        switch (requestType) {
            case "Track":
                return musicLibrary.getRandomTrack().getTitle();
            case "Author":
                return musicLibrary.getRandomTrack().getAuthor();
            case "Album":
                return musicLibrary.getRandomTrack().getAlbum();
            case "Artist":
                return musicLibrary.getRandomTrack().getArtist();
            default:
                return "Unknown";
        }
    }

    private void processRequests() {
        List<Request> allRequests = new ArrayList<>(requestQueue.getRequests());
        boolean isRequestProcessed = false;

        for (Request request : allRequests) {
            // Находим трек для заявки
            MusicTrack bestTrack = findTrackForRequest(request);

            if (bestTrack != null) {
                for (RadioProgram program : radioPrograms) {
                    if (Objects.equals(program.getType(), "По заявкам слушателей")
                            && program.getGenre() == bestTrack.getGenre()
                            && isDiverse(bestTrack, program)) {
                        // Добавляем трек в программу
                        boolean addedToProgram = addTrackToRadioProgram(bestTrack);
                        if (addedToProgram) {
                            // Добавляем заявку в выполненные
                            completedRequests.add(request);
                            requestQueue.removeRequest(request);
                            incrementGenrePopularity(bestTrack.getGenre());
                            System.out.println("Обработана заявка на трек: " + bestTrack.getTitle());
                            isRequestProcessed = true;
                            break; // Переходим к следующей заявке
                        }
                    }
                }
            }

            // Если заявку не удалось обработать, переносим её в конец очереди
            if (!isRequestProcessed) {
                requestQueue.removeRequest(request);
                requestQueue.addRequest(request);
            } else {
                isRequestProcessed = false; // Сбрасываем для следующей заявки
            }
        }

        // Проверка: если остались необработанные заявки
        if (requestQueue.getRequests().size() == allRequests.size()) {
            System.out.println("Нет подходящего трека для обработки заявок.");
        }
    }


    private MusicTrack findBestTrack(List<Request> requests) {
        Map<MusicTrack, Integer> trackRequestCount = new HashMap<>();

        for (Request request : requests) {
            MusicTrack track = findTrackForRequest(request);
            if (track != null) {
                trackRequestCount.put(track, trackRequestCount.getOrDefault(track, 0) + 1);
            }
        }

        return trackRequestCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private boolean isDiverse(MusicTrack track, RadioProgram program) {
        return !track.getArtist().equals(program.getLastPlayedArtist());
    }

    private boolean addTrackToRadioProgram(MusicTrack track) {
        for (RadioProgram program : radioPrograms) {
            System.out.println(program.getName() + " " + program.getTotalDuration() + " " + program.getLastPlayedArtist());
            if (Objects.equals(program.getType(), "По заявкам слушателей")
                    && program.getGenre() == track.getGenre()
                    && !program.isComplete()
                    && isDiverse(track, program)) {
                program.addTrack(track);
                program.setLastPlayedArtist(track.getArtist());
                return true;
            }
        }
        return false;
    }

    private MusicTrack findTrackForRequest(Request request) {
        switch (request.getType()) {
            case "Track":
                return musicLibrary.getMusicTracks().stream()
                        .filter(t -> t.getTitle().equals(request.getValue()))
                        .findFirst().orElse(null);
            case "Author":
                return musicLibrary.getMusicTracks().stream()
                        .filter(t -> t.getAuthor().equals(request.getValue()))
                        .findFirst().orElse(null);
            case "Album":
                return musicLibrary.getMusicTracks().stream()
                        .filter(t -> t.getAlbum().equals(request.getValue()))
                        .findFirst().orElse(null);
            case "Artist":
                return musicLibrary.getMusicTracks().stream()
                        .filter(t -> t.getArtist().equals(request.getValue()))
                        .findFirst().orElse(null);
            default:
                return null;
        }
    }

    private void incrementGenrePopularity(Genre genre) {
        genrePopularity.put(genre, genrePopularity.getOrDefault(genre, 0) + 1);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ObservableList<Request> getCompletedRequests() {
        return completedRequests;
    }

    public String getGenrePopularity() {
        String genresPopularityStatistics = "Топ 5 жанров:\n";
        for (Map.Entry<Genre, Integer> entry : genrePopularity.entrySet()) {
            genresPopularityStatistics += "Жанр: " + entry.getKey() + " | Популярность: " + entry.getValue() + '\n';
        }
        return genresPopularityStatistics.substring(0, genresPopularityStatistics.length() - 1);
    }
}
