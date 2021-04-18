0. mvn -N io.takari:maven:wrapper
1. Run deploy.sh
2. curl -X POST \
   -H "content-type: application/json" \
   -d '{"tree_id":1, "prop": "Стоимость"}' \
   http://localhost:8888/calculate


P.S. В проекте есть TODO: на текущий момент не реализована вложенность компонент
