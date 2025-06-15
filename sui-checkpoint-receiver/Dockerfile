# Stage 1: Builder
FROM rust:latest as builder

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    clang \
    llvm-dev \
    libclang-dev \
    cmake \
    build-essential \
    pkg-config \
    git \
    curl \
    libssl-dev \
    librdkafka-dev \
    protobuf-compiler \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ENV CARGO_TERM_COLOR=always
ENV RUST_BACKTRACE=1

WORKDIR /usr/app

COPY Cargo.toml Cargo.lock ./

COPY ./src ./src

RUN cargo check --release

RUN cargo build --bin remote_reader --release

# Stage 2: Runtime
FROM debian:bookworm-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    ca-certificates && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder /usr/app/target/release/remote_reader /usr/local/bin/remote_reader

CMD ["remote_reader"]