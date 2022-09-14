docker login -u "fvandaalen" -p "GxTgeu8i7iNvXa3" docker.io

IMAGE=fvandaalen/carrier:modelexposer_bayesian
docker build -t modelexposer_bayesian .

docker tag modelexposer_bayesian $IMAGE

docker push $IMAGE