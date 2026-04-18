INSERT INTO donation_cause
(code, status, display_order, allow_custom_amount, min_amount, max_amount, currency_code, active_from, active_to, image_url, created_at, updated_at, created_by, updated_by)
VALUES
('GENERAL', 'ACTIVE', 1, true, 5.00, 10000.00, 'USD', NOW(), NULL, 'https://example.com/images/general.jpg', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('FOOD_SUPPORT', 'ACTIVE', 2, true, 10.00, 5000.00, 'USD', NOW(), NULL, 'https://example.com/images/food.jpg', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO donation_cause_i18n
(cause_id, language_code, title, short_description, full_description, cta_label, thank_you_message, created_at, updated_at, created_by, updated_by)
VALUES
(1, 'en', 'General Donation', 'Support our general mission', 'Your contribution supports our overall mission and operating needs.', 'Donate Now', 'Thank you for your generous support.', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
(1, 'te', 'సాధారణ విరాళం', 'మా సాధారణ సేవలకు మద్దతు ఇవ్వండి', 'మీ విరాళం మా ప్రధాన సేవలు మరియు నిర్వహణ అవసరాలకు తోడ్పడుతుంది.', 'ఇప్పుడే విరాళం ఇవ్వండి', 'మీ ఔదార్యానికి ధన్యవాదాలు.', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
(2, 'en', 'Food Support', 'Help support food distribution', 'Your donation helps provide food support to people in need.', 'Support Food', 'Thank you for helping feed those in need.', NOW(), NOW(), 'SYSTEM', 'SYSTEM');