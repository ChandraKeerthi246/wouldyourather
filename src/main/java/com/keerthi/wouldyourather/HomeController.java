package com.keerthi.wouldyourather;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {
    private final GameResultRepository gameResultRepository;

public HomeController(GameResultRepository gameResultRepository) {
    this.gameResultRepository = gameResultRepository;
}

    private final List<Question> questions = List.of(
            new Question("Own a Dragon", "🐉", "DRAGON", "Own a Spaceship", "🚀", "SPACE"),
            new Question("Unlimited Pizza Forever", "🍕", "FOOD", "Magic Wand With 3 Wishes Per Year", "🪄", "MAGIC"),
            new Question("Become Ruler Of A Kingdom", "👑", "KINGDOM", "Explore The Universe For Free", "🌌", "SPACE"),
            new Question("Have A Loyal Pet Dinosaur", "🦖", "DRAGON", "Be Able To Talk To Ghosts", "👻", "CHAOS"),
            new Question("Pause Time Whenever You Want", "⏳", "MAGIC", "Read Anyone's Mind", "🧠", "CHAOS"),
            new Question("Own A Giant Castle", "🏰", "KINGDOM", "Attend A Real Hogwarts", "🧙", "MAGIC"),
            new Question("Eat Anything Without Gaining Weight", "🍔", "FOOD", "Own A Flying Unicorn", "🦄", "CHAOS"),
            new Question("Command Dragons In Battle", "🔥", "DRAGON", "Have An Army Of Robots", "🤖", "SPACE"),
            new Question("Have Unlimited Wealth", "💰", "KINGDOM", "Eat Any Food Without Paying", "🍟", "FOOD"),
            new Question("Open A Mystery Box That Changes Your Life", "🎁", "CHAOS", "Learn Every Spell Ever Written", "📖", "MAGIC")
    );

    @GetMapping("/")
public String showStart() {
    return "start";
}

@PostMapping("/start")
public String start(@RequestParam String nickname, HttpSession session) {
    session.setAttribute("nickname", nickname);
    session.setAttribute("index", 0);
    session.setAttribute("scores", new HashMap<String, Integer>());
    session.setAttribute("chosen", new ArrayList<String>());
    return "redirect:/question";
}

    @GetMapping("/question")
    public String question(HttpSession session, Model model) {
        Integer index = (Integer) session.getAttribute("index");

        if (index == null) return "redirect:/";
        if (index >= questions.size()) return "redirect:/result";

        Question q = questions.get(index);

        model.addAttribute("questionNumber", index + 1);
        model.addAttribute("totalQuestions", questions.size());

        model.addAttribute("optionA", q.getOptionA());
        model.addAttribute("imageA", q.getImageA());
        model.addAttribute("typeA", q.getTypeA());

        model.addAttribute("optionB", q.getOptionB());
        model.addAttribute("imageB", q.getImageB());
        model.addAttribute("typeB", q.getTypeB());

        return "index";
    }

    @PostMapping("/choose")
    public String choose(@RequestParam String choice,
                         @RequestParam String type,
                         HttpSession session) {

        Integer index = (Integer) session.getAttribute("index");
        Map<String, Integer> scores = (Map<String, Integer>) session.getAttribute("scores");
        List<String> chosen = (List<String>) session.getAttribute("chosen");

        if (index == null || scores == null || chosen == null) return "redirect:/";

        scores.put(type, scores.getOrDefault(type, 0) + 1);
        chosen.add(choice);

        session.setAttribute("index", index + 1);
        session.setAttribute("scores", scores);
        session.setAttribute("chosen", chosen);

        return "redirect:/question";
    }

    @GetMapping("/result")
    public String result(HttpSession session, Model model) {
        Map<String, Integer> scores = (Map<String, Integer>) session.getAttribute("scores");
        List<String> chosen = (List<String>) session.getAttribute("chosen");

        if (scores == null || chosen == null) return "redirect:/";

        String topType = scores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("CHAOS");
                String nickname = (String) session.getAttribute("nickname");

String title = getTitle(topType);
String threat = getThreat(topType);
String danger = getDanger(topType);


String answers = String.join(", ", chosen);

GameResult gameResult = new GameResult(
        nickname,
        title,
        threat,
        danger,
        answers
);

gameResultRepository.save(gameResult);
                

        model.addAttribute("chosen", chosen);
       model.addAttribute("nickname", nickname);
model.addAttribute("title", title);
model.addAttribute("threat", threat);
model.addAttribute("danger", danger);
        model.addAttribute("probability", getProbability(topType));
        model.addAttribute("sideEffects", getSideEffects(topType));
        model.addAttribute("status", getStatus(topType));

        return "result";
        
    }

    private String getTitle(String type) {
        return switch (type) {
            case "DRAGON" -> "🐉 Dragon Hoarder";
            case "SPACE" -> "🚀 Galactic Menace";
            case "FOOD" -> "🍕 Pizza Overlord";
            case "MAGIC" -> "🧙 Reality Glitch";
            case "KINGDOM" -> "👑 Kingdom Starter";
            default -> "🦄 Chaos Unicorn";
        };
    }

    private String getThreat(String type) {
        return switch (type) {
            case "DRAGON" -> "EXTREME";
            case "SPACE" -> "INTERPLANETARY";
            case "FOOD" -> "DELICIOUS";
            case "MAGIC" -> "ARCANE";
            case "KINGDOM" -> "ROYAL";
            default -> "UNCLASSIFIED";
        };
    }

    private String getDanger(String type) {
        return switch (type) {
            case "DRAGON" -> "9/10";
            case "SPACE" -> "10/10";
            case "FOOD" -> "6/10";
            case "MAGIC" -> "8/10";
            case "KINGDOM" -> "9/10";
            default -> "???/10";
        };
    }

    private String getProbability(String type) {
        return switch (type) {
            case "DRAGON" -> "Probability of accidentally starting a kingdom: 87%";
            case "SPACE" -> "Probability of being banned from Earth: 94%";
            case "FOOD" -> "Probability of solving every problem with food: 99%";
            case "MAGIC" -> "Probability of breaking reality before breakfast: 91%";
            case "KINGDOM" -> "Probability of declaring yourself ruler by Tuesday: 88%";
            default -> "Probability of becoming a local legend: 96%";
        };
    }

    private List<String> getSideEffects(String type) {
        return switch (type) {
            case "DRAGON" -> List.of(
                    "Refers to dragons as large emotional support lizards",
                    "Has a treasure room somewhere",
                    "Solves conflicts by adding more dragons",
                    "Cannot be trusted around ancient maps"
            );
            case "SPACE" -> List.of(
                    "Thinks gravity is optional",
                    "Owns maps of places nobody has visited",
                    "Says 'trust me' before every disaster",
                    "Frequently disappears for space errands"
            );
            case "FOOD" -> List.of(
                    "Carries emergency snacks",
                    "Judges people by pizza toppings",
                    "Treats buffets like sporting events",
                    "Has never trusted a small portion size"
            );
            case "MAGIC" -> List.of(
                    "Accidentally summons things",
                    "Uses magic for minor inconveniences",
                    "Has arguments with time itself",
                    "Frequently forgets which dimension they are in"
            );
            case "KINGDOM" -> List.of(
                    "Gives speeches nobody asked for",
                    "Collects crowns",
                    "Names pets after generals",
                    "Creates laws during lunch breaks"
            );
            default -> List.of(
                    "Makes decisions based entirely on vibes",
                    "Somehow survives impossible situations",
                    "Owns at least one cursed object",
                    "Turns ordinary days into stories nobody believes"
            );
        };
    }

    private String getStatus(String type) {
        return switch (type) {
            case "DRAGON" -> "Under observation by the Dragon Regulation Agency.";
            case "SPACE" -> "Wanted in 7 star systems.";
            case "FOOD" -> "Banned from all-you-can-eat restaurants.";
            case "MAGIC" -> "Magic license permanently suspended.";
            case "KINGDOM" -> "Several nations are concerned.";
            default -> "Too unpredictable for classification.";
        };
    }
}