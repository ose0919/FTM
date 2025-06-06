import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// EmotionHistoryPanel 클래스: 감정 기록 테이블 및 그래프 UI
class EmotionHistoryPanel extends JPanel {
    private User currentUser;
    private EmotionHistoryManager emotionHistoryManager;

    private JTable emotionHistoryTable;
    private DefaultTableModel emotionHistoryTableModel;
    private JTextField happyField, sadField, angryField;
    private JTextField dateField;
    private JPanel chartPanelContainer; // 그래프를 담을 패널

    public EmotionHistoryPanel(User user, EmotionHistoryManager emotionHistoryManager) {
        this.currentUser = user;
        this.emotionHistoryManager = emotionHistoryManager;

        setLayout(new BorderLayout());
        initComponents();
        loadInitialData(); // 테스트용 초기 데이터 로드 (Uimanager에서 이동)
    }

    private void initComponents() {
        // 감정 입력 필드
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        happyField = new JTextField("0");
        sadField = new JTextField("0");
        angryField = new JTextField("0");
        JButton recordEmotionButton = new JButton("감정 기록");
        recordEmotionButton.addActionListener(e -> recordEmotion());

        inputPanel.add(new JLabel("날짜 (yyyy-MM-dd):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("행복 (0-10):"));
        inputPanel.add(happyField);
        inputPanel.add(new JLabel("슬픔 (0-10):"));
        inputPanel.add(sadField);
        inputPanel.add(new JLabel("화남 (0-10):"));
        inputPanel.add(angryField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(recordEmotionButton);

        add(inputPanel, BorderLayout.NORTH);

        // 감정 기록 테이블 및 그래프
        JPanel historyDisplayPanel = new JPanel(new GridLayout(2, 1));

        // 테이블
        String[] columnNames = {"날짜", "행복", "슬픔", "화남"};
        emotionHistoryTableModel = new DefaultTableModel(columnNames, 0);
        emotionHistoryTable = new JTable(emotionHistoryTableModel);
        emotionHistoryTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(emotionHistoryTable);
        historyDisplayPanel.add(tableScrollPane);

        // 그래프 (ChartPanel)
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setBorder(BorderFactory.createTitledBorder("주간 감정 변화 그래프"));
        historyDisplayPanel.add(chartPanelContainer);

        add(historyDisplayPanel, BorderLayout.CENTER);

        //updateEmotionHistoryTableAndChart();
    }

    private void loadInitialData() {
        // 테스트용 초기 감정 기록 데이터
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 3), 1, 5, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 4), 1, 6, 3);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 5), 4, 4, 4);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 6), 3, 1, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 7), 6, 1, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 8), 2, 2, 2);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 9), 1, 1, 5);
        // updateEmotionHistoryTableAndChart();
    }

    private void recordEmotion() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int happy = Integer.parseInt(happyField.getText());
            int sad = Integer.parseInt(sadField.getText());
            int angry = Integer.parseInt(angryField.getText());

            if (happy < 0 || happy > 10 || sad < 0 || sad > 10 || angry < 0 || angry > 10) {
                JOptionPane.showMessageDialog(this, "감정 점수는 0에서 10 사이로 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            emotionHistoryManager.addEmotionEntry(currentUser.getId(), date, happy, sad, angry);
            JOptionPane.showMessageDialog(this, "감정 기록이 완료되었습니다.");
            //updateEmotionHistoryTableAndChart();

            dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            happyField.setText("0");
            sadField.setText("0");
            angryField.setText("0");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "행복, 슬픔, 화남은 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)", "입력 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void updateEmotionHistoryTableAndChart() {
        emotionHistoryTableModel.setRowCount(0);
        List<EmotionEntry> history = emotionHistoryManager.getEmotionHistoryForUser(currentUser.getId());

        int startIndex = Math.max(0, history.size() - 7);
        for (int i = startIndex; i < history.size(); i++) {
            EmotionEntry entry = history.get(i);
            emotionHistoryTableModel.addRow(new Object[]{
                    entry.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    entry.getHappy(),
                    entry.getSad(),
                    entry.getAngry()
            });
        }

        chartPanelContainer.removeAll();
       // ChartPanel chartPanel = emotionHistoryManager.createEmotionChartPanel(currentUser.getId());
       // chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }*/
}