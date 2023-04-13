CREATE TABLE `model_type` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
                              `show_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
                              `status` int DEFAULT '1',
                              `sort_key` int DEFAULT '1',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;


INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (1, 'Checkpoint', 'Checkpoint', 1, 10);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (2, 'TextualInversion', 'Textual Inversion', 1, 9);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (3, 'Hypernetwork', 'Hypernetwork', 1, 8);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (4, 'AestheticGradient', 'Aesthetic Gradient', 1, 7);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (5, 'LORA', 'LoRA', 1, 6);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (6, 'LoCon', 'LyCORIS', 1, 5);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (7, 'Controlnet', 'Controlnet', 1, 4);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (8, 'Poses', 'Poses', 1, 3);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (9, 'Wildcards', 'Wildcards', 1, 2);
INSERT INTO `model_type`(`id`, `type`, `show_type`, `status`, `sort_key`) VALUES (10, 'Other', 'Other', 1, 1);
