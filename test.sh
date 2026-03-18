#!/bin/bash

BASE_URL="http://localhost:8081/submit"

echo "======================================="
echo "  TEST POC NOTIFICATION"
echo "======================================="

# ─────────────────────────────────────────
# Test 1 : VIREMENT → C001 → SMS
# ─────────────────────────────────────────
echo ""
echo "[1/3] VIREMENT - Client C001 (préfère SMS)"
curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "C001",
    "operationType": "VIREMENT",
    "metadata": {
      "amount": 5000,
      "currency": "MAD",
      "beneficiary": "Ahmed"
    }
  }'
echo ""

sleep 1

# ─────────────────────────────────────────
# Test 2 : PAIEMENT → C002 → EMAIL
# ─────────────────────────────────────────
echo ""
echo "[2/3] PAIEMENT - Client C002 (préfère EMAIL)"
curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "C002",
    "operationType": "PAIEMENT",
    "metadata": {
      "amount": 1200,
      "currency": "MAD",
      "merchant": "Marjane"
    }
  }'
echo ""

sleep 1

# ─────────────────────────────────────────
# Test 3 : RETRAIT → C003 → PUSH
# ─────────────────────────────────────────
echo ""
echo "[3/3] RETRAIT - Client C003 (préfère PUSH)"
curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "C003",
    "operationType": "RETRAIT",
    "metadata": {
      "amount": 500,
      "currency": "MAD"
    }
  }'
echo ""

# ─────────────────────────────────────────
# Attendre que Kafka Streams traite les messages
# ─────────────────────────────────────────
echo ""
echo "Attente traitement Kafka Streams (3s)..."
sleep 3

# ─────────────────────────────────────────
# Afficher les résultats
# ─────────────────────────────────────────
echo ""
echo "======================================="
echo "  RESULTATS"
echo "======================================="

echo ""
echo "--- sms_output.txt ---"
if [ -f sms_output.txt ]; then cat sms_output.txt; else echo "(vide)"; fi

echo ""
echo "--- email_output.txt ---"
if [ -f email_output.txt ]; then cat email_output.txt; else echo "(vide)"; fi

echo ""
echo "--- push_output.txt ---"
if [ -f push_output.txt ]; then cat push_output.txt; else echo "(vide)"; fi

echo ""
echo "======================================="
echo "  FIN DES TESTS"
echo "======================================="
