package ua.goit;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends TelegramLongPollingBot {
    private Map<Long, Integer> levels = new HashMap<>();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return "BanderogusBbnBot";
    }

    @Override
    public String getBotToken() {
        return "5807086545:AAHArA26FIYjxTfplkh_DtCdEQ0ZiQIFyf4";
    }


    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            //Send image level 1
            sendImage("level-1", chatId);

            //Send message
            SendMessage message = createMessage("Ґа-ґа-ґа!\n" +
                    "Вітаємо у боті біолабораторії «Батько наш Бандера».\n" +
                    "\n" +
                    "Ти отримуєш гусака №71\n" +
                    "\n" +
                    "Цей бот ми створили для того, щоб твій гусак прокачався з рівня звичайної свійської худоби до рівня біологічної зброї, здатної нищити ворога. \n" +
                    "\n" +
                    "Щоб звичайний гусак перетворився на бандерогусака, тобі необхідно:\n" +
                    "✔️виконувати завдання\n" +
                    "✔️переходити на наступні рівні\n" +
                    "✔️заробити достатню кількість монет, щоб придбати Джавеліну і зробити свєрхтра-та-та.\n" +
                    "\n" +
                    "*Гусак звичайний.* Стартовий рівень.\n" +
                    "Бонус: 5 монет.\n" +
                    "Обери завдання, щоб перейти на наступний рівень");
            message.setChatId(chatId);

            attachButtons(message, Map.of(
                    "Сплести маскувальну сітку (+15 монет)", "level_1_task",
                    "Зібрати кошти патріотичними піснями (+15 монет)", "level_1_task",
                    "Вступити в Міністерство Мемів України (+15 монет)", "level_1_task",
                    "Запустити волонтерську акцію (+15 монет)", "level_1_task",
                    "Вступити до лав тероборони (+15 монет)", "level_1_task"
            ));

            sendApiMethodAsync(message);


        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("level_1_task") && getLevel(chatId) == 1) {
                //increase level
                setLevel(chatId, 2);
                //Send image
                sendImage("level-2", chatId);

                //Send message
                SendMessage message = createMessage("*Вітаємо на другому рівні! Твій гусак - біогусак.*\n" +
                        "Баланс: 20 монет. \n" +
                        "Обери завдання, щоб перейти на наступний рівень");
                message.setChatId(chatId);

                attachButtons(message, Map.of(
                        "Зібрати комарів для нової біологічної зброї (+15 монет)", "level_2_task",
                        "Пройти курс молодого бійця (+15 монет)", "level_2_task",
                        "Задонатити на ЗСУ (+15 монет)", "level_2_task",
                        "Збити дрона банкою огірків (+15 монет)", "level_2_task",
                        "Зробити запаси коктейлів Молотова (+15 монет)", "level_2_task"
                ));
                sendApiMethodAsync(message);

            }

            if (update.getCallbackQuery().getData().equals("level_2_task") && getLevel(chatId) == 2) {
                //increase level
                setLevel(chatId, 3);
                //Send image
                sendImage("level-3", chatId);

                //Send message
                SendMessage message = createMessage("*Вітаємо на третьому рівні! Твій гусак - бандеростажер.*\n" +
                        "Баланс: 35 монет. \n" +
                        "Обери завдання, щоб перейти на наступний рівень");
                message.setChatId(chatId);

                attachButtons(message, Map.of(
                        "Злітати на тестовий рейд по чотирьох позиціях (+15 монет)", "level_3_task",
                        "Відвезти гуманітарку на передок (+15 монет)", "level_3_task",
                        "Знайти зрадника та здати в СБУ (+15 монет)", "level_3_task",
                        "Навести арту на орків (+15 монет)", "level_3_task",
                        "Притягнути танк трактором (+15 монет)", "level_3_task"
                ));

                sendApiMethodAsync(message);
            }


        }

    }


    public Long getChatId(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getFrom().getId();
        } else {
            chatId = update.getCallbackQuery().getFrom().getId();
        }
        return chatId;
    }

    public SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        return message;
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);

            keyboard.add(Arrays.asList(button));

        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    public void sendImage(String name, Long chatId) {
        SendAnimation animation = new SendAnimation();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File("images/" + name + ".gif"));

        animation.setAnimation(inputFile);
        animation.setChatId(chatId);

        executeAsync(animation);
    }

    public int getLevel(Long chatId) {
        return levels.getOrDefault(chatId, 1);
    }

    public void setLevel(Long chatId, int level) {
        levels.put(chatId, level);
    }
}