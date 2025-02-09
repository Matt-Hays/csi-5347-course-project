# CSI5347 Distributed Systems

## Clone the Repo

This project contains submodules and therefore should be cloned via:

```
git clone --recurse-submodules git@github.com:ronwellman/CSI5324_Group_Project.git
```

If the repo has already been pulled in and the submodules are empty:

```
git submodule update --init
```

To pull all updates from all submodules

```
git submodule update --recursive --remote
```

## Compiling

```
mvn clean package spring-boot:build-image
```


## Running

```
docker-compose -f docker/docker-compose.yml up
```
