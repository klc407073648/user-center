#include <drogon/drogon.h>

using namespace drogon;

int main()
{
    app().addListener("127.0.0.1", 8082).run();
}