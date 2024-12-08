package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RadioStation {
    private final MusicLibrary musicLibrary; // Фонотека
    private final ObservableList<RadioProgram> radioPrograms; // Радиопрограммы
    private final RequestQueue requestQueue; // Очередь запросов
    private final ObservableList<Request> completedRequests; // Завершенные запросы
    private final HashSet<MusicTrack> playedTracksList;

    private int totalSimulationDays; // Длительность симуляции (в днях)
    private int simulationStepSeconds; // Шаг симуляции (в секундах)
    private int currentDay; // Текущий день симуляции
    private int currentStep; // Текущий шаг симуляции

    private boolean isRunning; // Флаг работы симуляции

    // Статистика по жанрам
    private final Map<Genre, Integer> genrePopularity;

    // Конструктор
    public RadioStation(MusicLibrary musicLibrary, ObservableList<RadioProgram> radioPrograms, RequestQueue requestQueue) {
        this.musicLibrary = musicLibrary;
        this.radioPrograms = radioPrograms;
        this.requestQueue = requestQueue;
        this.completedRequests = FXCollections.observableArrayList();
        this.playedTracksList = new HashSet<MusicTrack>();
        this.genrePopularity = new HashMap<>();
        this.isRunning = false;

        // Инициализация жанров
        for (Genre genre : Genre.values()) {
            genrePopularity.put(genre, 0);
        }
    }

    // Настройка начальных параметров симуляции
    public void configureSimulation(int totalSimulationDays, int simulationStepSeconds) {
        this.totalSimulationDays = totalSimulationDays;
        this.simulationStepSeconds = simulationStepSeconds;
        this.currentDay = 0;
        this.currentStep = 0;

    }

    // Сброс плейлистов программ
    private void resetProgramsPlaylists(){
        for (RadioProgram program: radioPrograms) {
            program.clearPlaylist();
            program.setLastPlayedArtist("");
        }
    }

    // Сброс плейлистов программ по заявкам слушателей
    private void resetAccordingToListenersRequestsProgramsPlaylists(){
        for (RadioProgram program: radioPrograms) {
            if (program instanceof AccordingToListenersRequests) {
                program.clearPlaylist();
            }
        }
    }

    // Запуск симуляции
    public void startSimulation() {
        resetProgramsPlaylists();
        musicLibrary.resetTracksPopularity();
        genrePopularity.replaceAll((genre, count) -> 0);
        completedRequests.clear();
        isRunning = true;
        currentDay = 1;
        currentStep = 0;
        System.out.println("Симуляция началась!");
    }

    // Остановка симуляции
    public void stopSimulation() {
        isRunning = false;
        System.out.println("Симуляция остановлена.");
    }

    // Проверка: завершилась ли симуляция
    public boolean canContinueSimulation() {
        return isRunning && currentDay <= totalSimulationDays;
    }

    // Симуляция шага (примерно 1 секунда)
    public void simulateStep() {
        if (!canContinueSimulation()) {
            stopSimulation();
            return;
        }

        generateRequests();

        processRequests();

        for (RadioProgram program : radioPrograms){
            if (program instanceof HitParade) {
                ((HitParade) program).GenerateHitParadePlaylist(musicLibrary);
            }
        }

        currentStep++;
        if (currentStep >= simulationStepSeconds) {
            currentStep = 0;
            currentDay++;
            if (currentDay <= totalSimulationDays) {
                resetAccordingToListenersRequestsProgramsPlaylists();
            }
            System.out.println("Наступил новый день симуляции: " + currentDay);
        }
    }

    // Случайная генерация запросов
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

    // Случайная генерация типа запроса
    private String generateRandomRequestType() {
        String[] requestTypes = {"Track", "Author", "Album", "Artist"};
        return requestTypes[new Random().nextInt(requestTypes.length)];
    }

    // Случайная генерация значения запроса
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

    // Обработка запросов
    private void processRequests() {
        List<Request> requestsToMove = new ArrayList<>();
        Iterator<Request> iterator = requestQueue.getRequests().iterator();

        while (iterator.hasNext()) {
            Request request = iterator.next();
            boolean isRequestProcessed = false;
            MusicTrack bestTrack = findTrackForRequest(request); // находим наиболее подходящий под запрос трек

            if (bestTrack != null) {
                for (RadioProgram program : radioPrograms) {
                    if (Objects.equals(program.getType(), "По заявкам слушателей")
                            && program.getGenre() == bestTrack.getGenre()
                            && isDiverse(bestTrack, program)) {
                        // Добавляем трек в программу
                        boolean addedToProgram = addTrackToRadioProgram(bestTrack);
                        if (addedToProgram) {
                            completedRequests.add(request);
                            playedTracksList.add(bestTrack);
                            iterator.remove(); // Удаляем заявку, если она была обработана
                            bestTrack.incrementPopularity();
                            incrementGenrePopularity(bestTrack.getGenre());
                            System.out.println("Обработана заявка на трек: " + bestTrack.getTitle());
                            isRequestProcessed = true;
                            break; // Переходим к следующей заявке
                        }
                    }
                }
            }
            if (!isRequestProcessed) {
                requestsToMove.add(request);  // Добавляем заявку в новый список
                iterator.remove();  // Удаляем заявку из текущего положения
            }
        }

        // Теперь обновляем основную очередь запросов
        requestQueue.getRequests().addAll(requestsToMove);

        // Проверка: если остались необработанные заявки
        if (requestQueue.getRequests().size() == completedRequests.size()) {
            System.out.println("Нет подходящего трека для обработки заявок.");
        }
    }

    // Проверяем разнообразие плейлиста
    private boolean isDiverse(MusicTrack track, RadioProgram program) {
        return !track.getArtist().equals(program.getLastPlayedArtist());
    }

    // Добавление трека в программу
    private boolean addTrackToRadioProgram(MusicTrack track) {
        for (RadioProgram program : radioPrograms) {
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

    // Поиск трека для запроса
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

    // Увеличение популярности трека
    private void incrementGenrePopularity(Genre genre) {
        genrePopularity.put(genre, genrePopularity.getOrDefault(genre, 0) + 1);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ObservableList<Request> getCompletedRequests() {
        return completedRequests;
    }

    // Статистика по проигранным трекам
    public String getPlayedTracks(){
        StringBuilder playedTracks = new StringBuilder("Список прозвучавших произведений:\n");

        for (MusicTrack track: playedTracksList) {
            playedTracks.append(track.toString())
                    .append("\n");
        }

        return playedTracks.substring(0, playedTracks.length() - 1);
    }

    // Топ самых популярных треков
    public String getTopRatedTracks(){
        StringBuilder topTracks = new StringBuilder("Самые рейтинговые произведения:\n");

        AtomicInteger index = new AtomicInteger(1);

        musicLibrary.getMusicTracks().stream()
                .sorted(Comparator.comparingInt(MusicTrack::getPopularity).reversed())
                .forEach(track -> topTracks.append(index.getAndIncrement())
                        .append(". ")
                        .append(track.toString())
                        .append(" ("+ track.getGenre() + ")")
                        .append("\n"));

        return topTracks.substring(0, topTracks.length() - 1);
    }

    // Топ 5 жанров
    public String getGenrePopularity() {
        StringBuilder genresPopularityStatistics = new StringBuilder("Топ 5 жанров:\n");

        // Сортировка жанров по популярности (по значению в Map)
        genrePopularity.entrySet().stream()
                .sorted(Map.Entry.<Genre, Integer>comparingByValue().reversed()) // Сортировка по убыванию
                .limit(5) // Берем только топ-5 жанров
                .forEach(entry -> genresPopularityStatistics.append(entry.getKey())
                        .append("\n"));

        return genresPopularityStatistics.substring(0, genresPopularityStatistics.length() - 1);
    }
}
