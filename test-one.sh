#!/bin/bash

BASE_URL="http://localhost:8081/submit"

echo "======================================="
echo "  TEST : VIREMENT - C001 -> SMS"
echo "======================================="

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
echo "Attente traitement (3s)..."
sleep 3

echo ""
echo "--- sms_output.txt ---"
if [ -f sms_output.txt ]; then cat sms_output.txt; else echo "(fichier vide ou inexistant)"; fi
