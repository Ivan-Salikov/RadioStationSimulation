package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Random;


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

    public RadioStation(MusicLibrary musicLibrary, ObservableList<RadioProgram> radioPrograms, RequestQueue requestQueue) {
        this.musicLibrary = musicLibrary;
        this.radioPrograms = radioPrograms;
        this.requestQueue = requestQueue;
        this.completedRequests = FXCollections.observableArrayList();
        this.isRunning = false;
    }

    public void configureSimulation(int totalSimulationDays, int simulationStepSeconds) {
        this.totalSimulationDays = totalSimulationDays;
        this.simulationStepSeconds = simulationStepSeconds;
        this.currentDay = 0;
        this.currentStep = 0;
    }

    public void startSimulation() {
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

        if (currentStep % 6 == 1) processRequests();

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

    // Метод обработки запросов с задержкой
    private void processRequests() {
        while (!requestQueue.isEmpty()) {
            long currentTime = System.currentTimeMillis();

            // Проверка, прошло ли достаточно времени для обработки следующего запроса
            if (currentTime - lastRequestTime >= REQUEST_DELAY) {
                Request request = requestQueue.processRequest();
                if (request != null) {
                    // Обработка запроса в зависимости от типа
                    MusicTrack track = null;
                    switch (request.getType()) {
                        case "Track":
                            track = musicLibrary.getMusicTracks().stream()
                                    .filter(t -> t.getTitle().equals(request.getValue()))
                                    .findFirst().orElse(null);
                            break;
                        case "Author":
                            track = musicLibrary.getMusicTracks().stream()
                                    .filter(t -> t.getAuthor().equals(request.getValue()))
                                    .findFirst().orElse(null);
                            break;
                        case "Album":
                            track = musicLibrary.getMusicTracks().stream()
                                    .filter(t -> t.getAlbum().equals(request.getValue()))
                                    .findFirst().orElse(null);
                            break;
                        case "Artist":
                            track = musicLibrary.getMusicTracks().stream()
                                    .filter(t -> t.getArtist().equals(request.getValue()))
                                    .findFirst().orElse(null);
                            break;
                    }
                    if (track != null) {
                        completedRequests.add(request);
                        System.out.println("Запрос обработан: " + track.getTitle());
                    } else {
                        System.out.println("Запрос не может быть обработан: " + request.getValue());
                    }

                    lastRequestTime = currentTime; // Обновление времени последней обработки запроса
                }
            }
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ObservableList<Request> getCompletedRequests() {
        return completedRequests;
    }
}
